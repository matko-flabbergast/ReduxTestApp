package com.example.reduxtestapp.data.poller

import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class PollerTest {
    
    private lateinit var testDispatcher: TestDispatcher
    private lateinit var testScope: TestScope
    private lateinit var poller: Poller
    
    @Before
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        testScope = TestScope(testDispatcher)
        Dispatchers.setMain(testDispatcher)
        poller = Poller(testScope)
    }
    
    @After
    fun tearDown() {
        poller.cancel()
        Dispatchers.resetMain()
    }
    
    @Test
    fun `getIntervals returns correct polling intervals`() {
        val intervals = poller.getIntervals()
        
        assertEquals(4, intervals.size)
        assertEquals(10.seconds, intervals[0])
        assertEquals(20.seconds, intervals[1])
        assertEquals(1.minutes, intervals[2])
        assertEquals(2.minutes, intervals[3])
    }
    
    @Test
    fun `startPolling executes function and waits for first interval`() = runTest {
        val pollFunction = mockk<suspend () -> Unit>(relaxed = true)
        
        poller.startPolling(pollFunction)
        
        // Verify initial execution
        verify(exactly = 1) { runBlocking { pollFunction() } }
        
        // Advance time by first interval (10 seconds)
        advanceTimeBy(10.seconds)
        
        // Should execute again
        verify(exactly = 2) { runBlocking { pollFunction() } }
    }
    
    @Test
    fun `polling follows increasing interval pattern`() = runTest {
        val pollFunction = mockk<suspend () -> Unit>(relaxed = true)
        
        poller.startPolling(pollFunction)
        
        // Initial execution
        verify(exactly = 1) { runBlocking { pollFunction() } }
        
        // After 10 seconds - second execution
        advanceTimeBy(10.seconds)
        verify(exactly = 2) { runBlocking { pollFunction() } }
        
        // After 20 more seconds - third execution
        advanceTimeBy(20.seconds)
        verify(exactly = 3) { runBlocking { pollFunction() } }
        
        // After 1 minute more - fourth execution
        advanceTimeBy(1.minutes)
        verify(exactly = 4) { runBlocking { pollFunction() } }
        
        // After 2 minutes more - fifth execution
        advanceTimeBy(2.minutes)
        verify(exactly = 5) { runBlocking { pollFunction() } }
        
        // After another 2 minutes - sixth execution (should continue with 2 minute interval)
        advanceTimeBy(2.minutes)
        verify(exactly = 6) { runBlocking { pollFunction() } }
    }
    
    @Test
    fun `isPolling returns correct status`() = runTest {
        assertFalse(poller.isPolling())
        
        val pollFunction = mockk<suspend () -> Unit>(relaxed = true)
        poller.startPolling(pollFunction)
        
        assertTrue(poller.isPolling())
        
        poller.stopPolling()
        assertFalse(poller.isPolling())
    }
    
    @Test
    fun `stopPolling cancels the polling job`() = runTest {
        val pollFunction = mockk<suspend () -> Unit>(relaxed = true)
        
        val job = poller.startPolling(pollFunction)
        assertTrue(job.isActive)
        assertTrue(poller.isPolling())
        
        poller.stopPolling()
        assertFalse(job.isActive)
        assertFalse(poller.isPolling())
    }
    
    @Test
    fun `startPolling stops existing polling before starting new one`() = runTest {
        val firstFunction = mockk<suspend () -> Unit>(relaxed = true)
        val secondFunction = mockk<suspend () -> Unit>(relaxed = true)
        
        val firstJob = poller.startPolling(firstFunction)
        assertTrue(firstJob.isActive)
        
        val secondJob = poller.startPolling(secondFunction)
        
        // First job should be cancelled
        assertFalse(firstJob.isActive)
        assertTrue(secondJob.isActive)
        
        // Only second function should be called going forward
        advanceTimeBy(10.seconds)
        verify(exactly = 0) { runBlocking { firstFunction() } }
        verify(exactly = 2) { runBlocking { secondFunction() } } // Initial + after 10s
    }
    
    @Test
    fun `error handler is called when poll function throws exception`() = runTest {
        val exception = RuntimeException("Test exception")
        val pollFunction = mockk<suspend () -> Unit> {
            coEvery { this@mockk() } throws exception
        }
        val errorHandler = mockk<suspend (Throwable) -> Unit>(relaxed = true)
        
        poller.startPolling(pollFunction, errorHandler)
        
        // Verify error handler was called
        coVerify(exactly = 1) { errorHandler(exception) }
        
        // Polling should continue despite the error
        advanceTimeBy(10.seconds)
        coVerify(exactly = 2) { errorHandler(exception) }
    }
    
    @Test
    fun `polling continues after non-cancellation exceptions`() = runTest {
        var callCount = 0
        val pollFunction = suspend {
            callCount++
            if (callCount <= 2) {
                throw RuntimeException("Test exception")
            }
        }
        val errorHandler = mockk<suspend (Throwable) -> Unit>(relaxed = true)
        
        poller.startPolling(pollFunction, errorHandler)
        
        // First call throws exception
        coVerify(exactly = 1) { errorHandler(any()) }
        
        // Wait for second interval
        advanceTimeBy(10.seconds)
        coVerify(exactly = 2) { errorHandler(any()) }
        
        // Wait for third interval - should succeed without calling error handler
        advanceTimeBy(20.seconds)
        coVerify(exactly = 2) { errorHandler(any()) } // Should still be 2
        
        assertEquals(3, callCount) // Function was called 3 times total
    }
    
    @Test
    fun `cancellation exception stops polling`() = runTest {
        val pollFunction = mockk<suspend () -> Unit> {
            coEvery { this@mockk() } throws CancellationException("Cancelled")
        }
        val errorHandler = mockk<suspend (Throwable) -> Unit>(relaxed = true)
        
        poller.startPolling(pollFunction, errorHandler)
        
        // Error handler should not be called for CancellationException
        coVerify(exactly = 0) { errorHandler(any()) }
        
        // Polling should stop
        advanceTimeBy(10.seconds)
        assertFalse(poller.isPolling())
    }
    
    @Test
    fun `cancel stops polling and cancels scope`() = runTest {
        val pollFunction = mockk<suspend () -> Unit>(relaxed = true)
        
        poller.startPolling(pollFunction)
        assertTrue(poller.isPolling())
        
        poller.cancel()
        
        assertFalse(poller.isPolling())
        
        // Advancing time should not trigger more calls
        val initialCallCount = 1 // Initial call
        advanceTimeBy(10.seconds)
        verify(exactly = initialCallCount) { runBlocking { pollFunction() } }
    }
}
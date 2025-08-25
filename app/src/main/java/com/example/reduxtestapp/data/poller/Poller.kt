package com.example.reduxtestapp.data.poller

import kotlinx.coroutines.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * A polling service that executes a suspending function at increasing intervals.
 * The polling strategy follows: 10s → 20s → 1min → 2min, then continues at 2min intervals.
 */
class Poller(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) {
    private var pollingJob: Job? = null
    private val intervals = listOf(
        10.seconds,
        20.seconds,
        1.minutes,
        2.minutes
    )

    /**
     * Starts polling with the given suspending function.
     * 
     * @param pollFunction The suspending function to execute on each poll
     * @param onError Optional error handler for exceptions during polling
     * @return The polling job that can be cancelled
     */
    fun startPolling(
        pollFunction: suspend () -> Unit,
        onError: (suspend (Throwable) -> Unit)? = null
    ): Job {
        stopPolling() // Stop any existing polling
        
        pollingJob = scope.launch {
            var intervalIndex = 0
            
            while (isActive) {
                try {
                    pollFunction()
                } catch (e: Exception) {
                    if (e is CancellationException) {
                        throw e // Re-throw cancellation to properly stop polling
                    }
                    onError?.invoke(e)
                }
                
                // Calculate delay - use the last interval (2 minutes) if we've exceeded the array
                val currentInterval = if (intervalIndex < intervals.size) {
                    intervals[intervalIndex]
                } else {
                    intervals.last() // Continue with 2 minutes
                }
                
                delay(currentInterval)
                
                // Move to next interval, but don't exceed the array bounds
                if (intervalIndex < intervals.size - 1) {
                    intervalIndex++
                }
            }
        }
        
        return pollingJob!!
    }

    /**
     * Stops the current polling operation.
     */
    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    /**
     * Returns true if polling is currently active.
     */
    fun isPolling(): Boolean {
        return pollingJob?.isActive == true
    }

    /**
     * Cancels the polling scope and stops all polling operations.
     * After calling this, the Poller instance should not be reused.
     */
    fun cancel() {
        stopPolling()
        scope.cancel()
    }

    /**
     * Gets the current polling intervals for testing purposes.
     */
    internal fun getIntervals(): List<Duration> = intervals
}
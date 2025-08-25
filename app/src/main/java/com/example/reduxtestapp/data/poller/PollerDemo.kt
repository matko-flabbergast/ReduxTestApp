package com.example.reduxtestapp.data.poller

import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds

/**
 * Simple console demonstration of the Poller functionality.
 * This can be run independently to test the polling behavior.
 */
suspend fun main() {
    println("Starting Poller demonstration...")
    
    val scope = CoroutineScope(Dispatchers.Default)
    val poller = Poller(scope)
    
    var executionCount = 0
    
    val pollFunction: suspend () -> Unit = {
        executionCount++
        println("Poll execution #$executionCount at ${System.currentTimeMillis()}")
        
        // Simulate some work
        delay(100)
    }
    
    val errorHandler: suspend (Throwable) -> Unit = { exception ->
        println("Error during polling: ${exception.message}")
    }
    
    println("Starting polling with increasing intervals: 10s -> 20s -> 1min -> 2min...")
    poller.startPolling(pollFunction, errorHandler)
    
    // Let it run for demonstration
    println("Polling started. Will run for 2 minutes for demonstration...")
    delay(120.seconds) // Run for 2 minutes
    
    println("Stopping polling...")
    poller.stopPolling()
    
    println("Polling stopped. Total executions: $executionCount")
    
    poller.cancel()
    println("Demonstration complete.")
}
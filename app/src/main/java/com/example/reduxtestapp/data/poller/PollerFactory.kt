package com.example.reduxtestapp.data.poller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Factory object for creating Poller instances with common configurations.
 */
object PollerFactory {
    
    /**
     * Creates a Poller with default IO scope suitable for network operations.
     */
    fun createIOPoller(): Poller {
        return Poller(CoroutineScope(Dispatchers.IO + SupervisorJob()))
    }
    
    /**
     * Creates a Poller with default scope suitable for background processing.
     */
    fun createDefaultPoller(): Poller {
        return Poller(CoroutineScope(Dispatchers.Default + SupervisorJob()))
    }
    
    /**
     * Creates a Poller with a custom scope.
     */
    fun createPoller(scope: CoroutineScope): Poller {
        return Poller(scope)
    }
}
package com.example.reduxtestapp.data.poller

import com.example.reduxtestapp.data.repository.TodoRepository
import com.example.reduxtestapp.redux.Action
import kotlinx.coroutines.CoroutineScope
import org.reduxkotlin.Store

/**
 * Example usage of the Poller class to periodically fetch todos.
 * This demonstrates how the Poller can be integrated with the existing Redux architecture.
 */
class TodoPollingService(
    private val todoRepository: TodoRepository,
    private val store: Store<*>,
    scope: CoroutineScope? = null
) {
    private val poller = scope?.let { Poller(it) } ?: PollerFactory.createIOPoller()
    
    /**
     * Starts polling for todo updates with increasing intervals.
     */
    fun startTodoPolling() {
        poller.startPolling(
            pollFunction = {
                // Fetch todos and dispatch update action
                val todos = todoRepository.getTodos()
                store.dispatch(Action.Todo.UpdateTodoList(todos))
            },
            onError = { exception ->
                // Handle polling errors (e.g., network issues)
                // Could dispatch error action or log the error
                println("Todo polling error: ${exception.message}")
            }
        )
    }
    
    /**
     * Stops todo polling.
     */
    fun stopTodoPolling() {
        poller.stopPolling()
    }
    
    /**
     * Returns whether todo polling is currently active.
     */
    fun isTodoPollingActive(): Boolean {
        return poller.isPolling()
    }
    
    /**
     * Cleanup resources.
     */
    fun cleanup() {
        poller.cancel()
    }
}

/**
 * Example of using the Poller for general network calls with the increasing interval strategy.
 */
class NetworkPollingService(
    private val todoRepository: TodoRepository,
    scope: CoroutineScope? = null
) {
    private val poller = scope?.let { Poller(it) } ?: PollerFactory.createIOPoller()
    
    /**
     * Starts polling a long network call with increasing intervals.
     */
    fun startNetworkPolling(onResult: (String) -> Unit) {
        poller.startPolling(
            pollFunction = {
                val result = todoRepository.longNetworkCall()
                onResult(result)
            },
            onError = { exception ->
                println("Network polling error: ${exception.message}")
            }
        )
    }
    
    fun stopNetworkPolling() {
        poller.stopPolling()
    }
    
    fun cleanup() {
        poller.cancel()
    }
}
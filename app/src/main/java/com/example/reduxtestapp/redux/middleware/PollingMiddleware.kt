package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.data.poller.Poller
import com.example.reduxtestapp.data.repository.TodoRepository
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.reduxkotlin.Dispatcher
import org.reduxkotlin.Store

/**
 * Middleware that integrates the Poller class with Redux for automatic todo fetching.
 * This demonstrates how the Poller can be seamlessly integrated into the existing Redux architecture.
 */
class PollingMiddleware(
    private val todoRepository: TodoRepository,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
) {
    private val poller = Poller(scope)

    fun pollingMiddleware(store: Store<AppState>) = { next: Dispatcher ->
        { action: Any ->
            when (action) {
                is Action.Todo.StartPolling -> {
                    startTodoPolling(store)
                }
                is Action.Todo.StopPolling -> {
                    stopTodoPolling()
                }
            }
            val result = next(action)
            result
        }
    }

    private fun startTodoPolling(store: Store<AppState>) {
        poller.startPolling(
            pollFunction = {
                // Fetch todos and dispatch update action
                val todos = todoRepository.getTodos()
                store.dispatch(Action.Todo.UpdateTodoList(todos))
            },
            onError = { exception ->
                // Handle polling errors - could dispatch error action
                store.dispatch(Action.Todo.PollingError(exception.message ?: "Unknown error"))
            }
        )
    }

    private fun stopTodoPolling() {
        poller.stopPolling()
    }

    /**
     * Cleanup method to be called when the middleware is no longer needed
     */
    fun cleanup() {
        poller.cancel()
    }
}

/**
 * Extension to the existing Action sealed interface to support polling actions
 * This would be added to the existing Actions.kt file
 */
sealed interface PollingAction : Action {
    data object StartPolling : Action
    data object StopPolling : Action
    data class PollingError(val message: String) : Action
}

// These would be added to the existing Action.Todo sealed interface:
// data object StartPolling : Action
// data object StopPolling : Action  
// data class PollingError(val message: String) : Action
package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.data.model.TodoItem
import com.example.reduxtestapp.data.repository.TodoRepository
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.todosReducer
import com.example.reduxtestapp.repositories.MockTodoRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createThreadSafeStore

class RepoMiddlewareTest {
    private lateinit var repo: TodoRepository
    private lateinit var store: Store<AppState>
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var middleware: RepoMiddleware
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        repo = MockTodoRepository()
        runBlocking {
            repo.insertTodo(
                TodoItem("Todo number 1", true)
            )
            repo.insertTodo(
                TodoItem("Todo number 2", false)
            )
            repo.insertTodo(
                TodoItem("Todo number 3", false)
            )
        }

        middleware = RepoMiddleware(repo)

        store = createThreadSafeStore(
            reducer = { state, action ->
                todosReducer(state, action)
            },
            preloadedState = AppState(),
            enhancer = applyMiddleware(
                CheckDispatchedActionsMiddleware::actionsMiddleware,
                RepoMiddleware(repo)::todoMiddleware
            )
        )

        Dispatchers.setMain(testDispatcher)


    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `dispatches UpdateTodoList action after AddTodo action`() = runTest {
        val testText = "Test Todo"
        store.dispatch(Action.AddTodo(testText))
        advanceUntilIdle()
        assert(CheckDispatchedActionsMiddleware.dispatchedActions.any{it is Action.UpdateTodoList})
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `dispatches UpdateTodoList action after ToggleTodo action`() = runTest {
        val index = 1
        store.dispatch(Action.ToggleTodo(index))
        advanceUntilIdle()
        assert(CheckDispatchedActionsMiddleware.dispatchedActions.any{it is Action.UpdateTodoList})
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `dispatches UpdateTodoList action after FetchTodos action`() = runTest {
        store.dispatch(Action.FetchTodos)
        advanceUntilIdle()
        assert(CheckDispatchedActionsMiddleware.dispatchedActions.any{it is Action.UpdateTodoList})
    }

}



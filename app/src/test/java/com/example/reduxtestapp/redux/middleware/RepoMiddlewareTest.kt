package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.data.repository.TodoRepository
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.todosReducer
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createThreadSafeStore

class RepoMiddlewareTest {
    private lateinit var repo: TodoRepository
    private lateinit var store: Store<AppState>
    private val testDispatcher = StandardTestDispatcher()

    // UNDER TEST
    private lateinit var middleware: RepoMiddleware

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {

        repo = mockk<TodoRepository>()

        coEvery { repo.getTodos() } returns listOf()

        middleware = RepoMiddleware(repo)

        store = createThreadSafeStore(
            reducer = { state, action ->
                todosReducer(state, action)
            },
            preloadedState = AppState(),
            enhancer = applyMiddleware(
                CheckDispatchedActionsMiddleware::actionsMiddleware,
                RepoMiddleware(repo)::todoMiddleware,
                AsyncMiddleware::asyncMiddleware
            )
        )

        Dispatchers.setMain(testDispatcher)


    }

    @Test
    fun `calls Repo to add Todo after AddTodo action`() = runTest {
        val testText = "Test Todo"

        coEvery { repo.insertTodo(any()) } returns listOf()

        store.dispatch(Action.AddTodo(testText))

        coVerify (exactly = 1) {
            repo.insertTodo(any())
        }

    }

    @Test
    fun `calls Repo to toggle Todo after ToggleTodo action`() = runTest {

        val index = 1
        coEvery { repo.toggleTodo(any()) } returns listOf()

        store.dispatch(Action.ToggleTodo(index))

        coVerify (exactly = 1) {
            repo.toggleTodo(index)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `dispatches UpdateTodoList action after AddTodo action`() = runTest {
        val testText = "Test Todo"
        coEvery { repo.insertTodo(any()) } returns listOf()
        store.dispatch(Action.AddTodo(testText))
        advanceUntilIdle()
        store.subscribe {
            assert(CheckDispatchedActionsMiddleware.dispatchedActions
                .any{ it is Action.UpdateTodoList }
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `dispatches UpdateTodoList action after ToggleTodo action`() = runTest {
        val index = 1
        coEvery { repo.toggleTodo(any()) } returns listOf()

        store.dispatch(Action.ToggleTodo(index))

        advanceUntilIdle()
        store.subscribe {
            assert(CheckDispatchedActionsMiddleware.dispatchedActions
                .any{it is Action.UpdateTodoList}
            )
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `dispatches UpdateTodoList action after FetchTodos action`() = runTest {
        store.dispatch(Action.FetchTodos)
        advanceUntilIdle()
        store.subscribe {
            assert(CheckDispatchedActionsMiddleware.dispatchedActions
                .any{it is Action.UpdateTodoList}
            )
        }
    }

}



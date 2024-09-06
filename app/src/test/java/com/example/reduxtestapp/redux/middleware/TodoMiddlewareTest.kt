package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.data.model.todo.TodoItem
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.data.repository.todo.TodoRepository
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.rootReducer
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

class TodoMiddlewareTest {
    private val repo = mockk<TodoRepository>()
    private val testDispatcher = StandardTestDispatcher()

    // UNDER TEST
    private val middleware = TodoMiddleware(repo)

    private fun createStore(): Store<AppState> =
        createThreadSafeStore(
            reducer = { state, action ->
                rootReducer(state, action)
            },
            preloadedState = AppState(),
            enhancer = applyMiddleware(
                CheckDispatchedActionsMiddleware::actionsMiddleware,
                middleware::todoMiddleware,
                AsyncMiddleware()::asyncMiddleware
            )
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {

        coEvery { repo.getTodos() } returns listOf()

        Dispatchers.setMain(testDispatcher)


    }


    @Test
    fun `calls Repo to add Todo after AddTodo action`() = runTest {
        val testText = "Test Todo"
        val store = createStore()

        coEvery { repo.insertTodo(any()) } returns listOf()

        store.dispatch(Action.Todo.AddTodo(testText))

        coVerify (exactly = 1) {
            repo.insertTodo(any())
        }

    }

    @Test
    fun `calls Repo to toggle Todo after ToggleTodo action`() = runTest {
        val store = createStore()

        val index = 1
        coEvery { repo.toggleTodo(any()) } returns listOf()

        store.dispatch(Action.Todo.ToggleTodo(index))

        coVerify (exactly = 1) {
            repo.toggleTodo(index)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `dispatches UpdateTodoList action after AddTodo action`() = runTest {
        val store = createStore()
        val testText = "Test Todo"
        coEvery { repo.insertTodo(any()) } returns listOf()
        store.dispatch(Action.Todo.AddTodo(testText))
        advanceUntilIdle()
        store.subscribe {
            assert(CheckDispatchedActionsMiddleware.dispatchedActions
                .any{ it is Action.Todo.UpdateTodoList }
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `dispatches UpdateTodoList action after ToggleTodo action`() = runTest {
        val store = createStore()
        val index = 1
        coEvery { repo.toggleTodo(any()) } returns listOf()

        store.dispatch(Action.Todo.ToggleTodo(index))

        advanceUntilIdle()
        store.subscribe {
            assert(CheckDispatchedActionsMiddleware.dispatchedActions
                .any{it is Action.Todo.UpdateTodoList}
            )
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `dispatches UpdateTodoList action after FetchTodos action`() = runTest {
        val store = createStore()
        store.dispatch(Action.Todo.FetchTodos)
        advanceUntilIdle()
        store.subscribe {
            assert(CheckDispatchedActionsMiddleware.dispatchedActions
                .any{it is Action.Todo.UpdateTodoList}
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `addTodo action updates state with added todo`() = runTest {
        val store = createStore()
        val testText = "Test Todo"
        val testTodo = TodoItem(testText, false)
        coEvery { repo.insertTodo(any()) } returns listOf(
            TodoItem(testText, false)
        )
        store.dispatch(Action.Todo.AddTodo(testText))
        advanceUntilIdle()
        store.subscribe {
            assert(store.state.todoList.contains(testTodo)
            )
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `toggleTodo action updates state with toggled todo`() = runTest {
        val store = createStore()
        val index = 0
        val testTodo = TodoItem("Test Todo", false)
        coEvery { repo.toggleTodo(any()) } returns listOf(
            testTodo
        )
        store.dispatch(Action.Todo.ToggleTodo(index))
        advanceUntilIdle()
        store.subscribe {
            assert(store.state.todoList.contains(testTodo)
            )
        }

    }

}



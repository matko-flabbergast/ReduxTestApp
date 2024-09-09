package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.data.model.todo.TodoDto
import com.example.reduxtestapp.data.repository.todo.TodoRepository
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.Middleware
import org.reduxkotlin.Store

class TodoMiddleware (
    private val repo: TodoRepository,
) : Middleware() {

    override fun middleware(store: Store<AppState>, action: Any) {
        when (action) {
            is Action.Todo.AddTodo -> {
                store.dispatch(Action.Async {
                    val newList = repo.insertTodo(
                        TodoDto(action.text, false)
                    )
                    store.dispatch(Action.Todo.UpdateTodoList(newList))
                })
            }
            is Action.Todo.ToggleTodo -> {
                store.dispatch(Action.Async {
                    val newList = repo.toggleTodo(action.index)
                    store.dispatch(Action.Todo.UpdateTodoList(newList))
                })
            }
            is Action.Todo.FetchTodos ->
                store.dispatch(Action.Async {
                    val todos = repo.getTodos()
                    store.dispatch(Action.Todo.UpdateTodoList(todos))
                })
        }
    }

}
package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.data.model.TodoItem
import com.example.reduxtestapp.data.repository.TodoRepository
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import org.reduxkotlin.Dispatcher
import org.reduxkotlin.Store

class RepoMiddleware (
    private val repo: TodoRepository,
) {

    fun todoMiddleware(store: Store<AppState>) = { next: Dispatcher ->
        { action: Any ->

            when (action) {
                is Action.AddTodo ->
                    store.dispatch(Action.Async {
                        val newList = repo.insertTodo(
                            TodoItem(action.text, false)
                        )
                        store.dispatch(Action.UpdateTodoList(newList))
                    })
                is Action.ToggleTodo ->
                    store.dispatch(Action.Async {
                        val newList = repo.toggleTodo(action.index)
                        store.dispatch(Action.UpdateTodoList(newList))
                    })
                is Action.FetchTodos ->
                    store.dispatch(Action.Async {
                        store.dispatch(Action.UpdateTodoList(repo.getTodos()))
                    })
            }
            val result = next(action)
            result

        }
    }

}
package com.example.reduxtestapp.redux.middleware

import android.util.Log
import com.example.reduxtestapp.data.model.TodoItem
import com.example.reduxtestapp.data.repository.TodoRepository
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.reduxkotlin.Dispatcher
import org.reduxkotlin.Store

class RepoMiddleware (
    private val repo: TodoRepository,
) {

    fun todoMiddleware(store: Store<AppState>) = { next: Dispatcher ->
        { action: Any ->

            when (action) {
                is Action.AddTodo -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        val newList = repo.insertTodo(
                            TodoItem(action.text, false)
                        )
                        store.dispatch(Action.UpdateTodoList(newList))
                    }
                }
                is Action.ToggleTodo -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        val newList = repo.toggleTodo(action.index)
                        store.dispatch(Action.UpdateTodoList(newList))
                    }
                }
                is Action.FetchTodos -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        store.dispatch(Action.UpdateTodoList(repo.getTodos()))
                    }
                }
            }
            val result = next(action)
            result

        }
    }

}
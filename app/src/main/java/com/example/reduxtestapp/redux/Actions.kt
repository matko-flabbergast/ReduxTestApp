package com.example.reduxtestapp.redux

import android.content.Context
import com.example.reduxtestapp.data.model.TodoItem

sealed class Action {
    data class AddTodo(val text: String) : Action()
    data class ToggleTodo(val index: Int): Action()
    data object FetchTodos: Action()
    data class Async(val asyncFunc: suspend () -> Unit): Action()
    data class UpdateTodoList(val items: List<TodoItem>): Action()
    data class SetVisibilityFilter(val filter: VisibilityFilter, val context: Context): Action()
}
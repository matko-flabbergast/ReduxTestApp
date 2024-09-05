package com.example.reduxtestapp.redux

import android.util.Log
import android.widget.Toast
import com.example.reduxtestapp.data.model.TodoItem

data class AppState (
    val todoList: List<TodoItem> = listOf(),
    val visibilityFilter: VisibilityFilter = VisibilityFilter.ALL
)

fun todosReducer(state: AppState, action: Any): AppState {

    return when (action) {
        is Action.UpdateTodoList -> {
            state.copy(
                todoList = action.items
            )
        }
        is Action.SetVisibilityFilter -> state.copy(
            visibilityFilter = state.visibilityFilter
        )
        else -> state
    }
}



data class TodoUiData (
    val text: String,
    val isCompleted: Boolean
)

enum class VisibilityFilter {
    ALL,
    COMPLETED,
    UNCOMPLETED
}
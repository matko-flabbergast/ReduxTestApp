package com.example.reduxtestapp.redux

import com.example.reduxtestapp.data.model.todo.TodoItem

data class AppState (
    val todoList: List<TodoItem> = listOf(),
    val visibilityFilter: VisibilityFilter = VisibilityFilter.ALL
)

fun todosReducer(state: AppState, action: Any): AppState {

    return when (action) {
        is Action.Todo.UpdateTodoList -> {
            state.copy(
                todoList = action.items
            )
        }
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
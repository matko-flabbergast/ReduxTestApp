package com.example.reduxtestapp.redux

import com.example.reduxtestapp.data.model.country.Country
import com.example.reduxtestapp.data.model.todo.TodoItem

data class AppState (
    val todoList: List<TodoItem> = listOf(),
    val countryList: List<Country> = listOf(),
    val isError: Boolean = false,
    val visibilityFilter: VisibilityFilter = VisibilityFilter.ALL
)

fun rootReducer(state: AppState, action: Any): AppState {
    return when (action) {
        is Action.Todo -> todosReducer(state, action)
        is Action.Country -> countryReducer(state, action)
        is Action.Error -> state.copy(
            isError = true
        )
        else -> state
    }
}

fun countryReducer(state: AppState, action: Action.Country): AppState {
    return when (action) {
        is Action.Country.UpdateCountryList -> {
            state.copy(
                countryList = action.items,
                isError = false
            )
        }
        else -> state.copy(
            isError = false
        )
    }
}

fun todosReducer(state: AppState, action: Action.Todo): AppState {
    return when (action) {
        is Action.Todo.UpdateTodoList -> {
            state.copy(
                todoList = action.items,
                isError = false
            )
        }
        else -> state.copy(
            isError = false
        )
    }
}

enum class VisibilityFilter {
    ALL,
    COMPLETED,
    UNCOMPLETED
}
package com.example.reduxtestapp.redux

import com.example.reduxtestapp.data.model.country.Country
import com.example.reduxtestapp.data.model.todo.TodoItem

data class AppState (
    val todoState: TodoState = TodoState(),
    val countryState: CountryState = CountryState(),
)

fun rootReducer(state: AppState, action: Any): AppState {
    return AppState(
        todoState = todosReducer(state, action),
        countryState = countryReducer(state, action),
    )
}

fun countryReducer(state: AppState, action: Any): CountryState {
    return when (action) {
        is Action.Country.UpdateCountryList -> {
            state.countryState.copy(
                countryList = action.items,
                status = CountryState.Status.SUCCESS
            )
        }
        is Action.Country.Error -> {
            state.countryState.copy(
                status = CountryState.Status.ERROR
            )
        }
        is Action.Country.GetCountries -> {
            state.countryState.copy(
                status = CountryState.Status.PENDING
            )
        }
        is Action.Country.SearchCountries -> {
            state.countryState.copy(
                status = CountryState.Status.PENDING
            )
        }
        else -> state.countryState
    }
}

fun todosReducer(state: AppState, action: Any): TodoState {
    return when (action) {
        is Action.Todo.UpdateTodoList -> {
            state.todoState.copy(
                todoList = action.items,
                status = TodoState.Status.SUCCESS
            )
        }
        is Action.Todo.Error -> {
            state.todoState.copy(
                status = TodoState.Status.ERROR
            )
        }
        is Action.Todo.AddTodo -> {
            state.todoState.copy(
                status = TodoState.Status.PENDING
            )
        }
        is Action.Todo.ToggleTodo -> {
            state.todoState.copy(
                status = TodoState.Status.PENDING
            )
        }
        is Action.Todo.FetchTodos -> {
            state.todoState.copy(
                status = TodoState.Status.PENDING
            )
        }
        else -> state.todoState
    }
}

data class TodoState (
    val todoList: List<TodoItem> = listOf(),
    val status: Status = Status.SUCCESS,
) {
    enum class Status { SUCCESS, PENDING, ERROR }
}

data class CountryState(
    val countryList: List<Country> = listOf(),
    val status: Status = Status.SUCCESS,
) {
    enum class Status { SUCCESS, PENDING, ERROR }
}

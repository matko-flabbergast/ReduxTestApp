package com.example.reduxtestapp.redux

import com.example.reduxtestapp.data.model.country.Country
import com.example.reduxtestapp.data.model.todo.TodoItem

sealed interface Action {
    sealed interface Todo : Action {
        data class AddTodo(val text: String) : Todo
        data class ToggleTodo(val index: Int): Todo
        data object FetchTodos: Todo
        data class UpdateTodoList(val items: List<TodoItem>): Todo
    }
    sealed interface Country : Action {
        data object GetCountries: Country
        data class UpdateCountryList(val items: List<com.example.reduxtestapp.data.model.country.Country>): Country
    }
    data class Error(val message: String = ""): Action
    data class Async(val asyncFunc: suspend () -> Unit): Action
}
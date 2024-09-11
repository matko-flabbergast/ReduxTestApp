package com.example.reduxtestapp.redux

import com.example.reduxtestapp.domain.model.country.CountryModel
import com.example.reduxtestapp.domain.model.todo.TodoModel

sealed interface Action {
    sealed interface Todo : Action {
        data class AddTodo(val text: String) : Todo
        data class ToggleTodo(val index: Int): Todo
        data class EditTodo(val index: Int, val text: String): Todo
        data class RemoveTodo(val index: Int): Todo
        data object FetchTodos: Todo
        data class UpdateTodoList(val items: List<TodoModel>): Todo
        data class Error(val message: String? = ""): Todo
    }
    sealed interface Country : Action {
        data object GetCountries: Country
        data class SearchCountries(val query: String): Country
        data object LoadInitialCountries : Country
        data class UpdateCountryList(val items: List<CountryModel>): Country
        data class SearchByLanguageAndCurrency(val language: String, val currency: String): Country
        data class Error(val message: String? = ""): Country

    }
    data class Async(val asyncFunc: suspend () -> Unit): Action
}
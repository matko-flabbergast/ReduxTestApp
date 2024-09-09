package com.example.reduxtestapp.redux

import com.example.reduxtestapp.redux.reducers.countryReducer
import com.example.reduxtestapp.redux.reducers.todosReducer
import com.example.reduxtestapp.redux.state.CountryState
import com.example.reduxtestapp.redux.state.TodoState

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

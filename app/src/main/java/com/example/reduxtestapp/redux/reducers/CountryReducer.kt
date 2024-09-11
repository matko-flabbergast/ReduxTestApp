package com.example.reduxtestapp.redux.reducers

import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.state.CountryState


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
        is Action.Country.LoadInitialCountries -> {
            state.countryState.copy(
                status = CountryState.Status.PENDING
            )
        }
        is Action.Country.SearchByLanguageAndCurrency -> {
            state.countryState.copy(
                status = CountryState.Status.PENDING
            )
        }
        else -> state.countryState
    }
}

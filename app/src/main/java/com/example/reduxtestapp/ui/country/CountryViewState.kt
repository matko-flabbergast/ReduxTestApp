package com.example.reduxtestapp.ui.country

import com.example.reduxtestapp.data.model.country.asPresentation
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.CountryState

data class CountryViewState (
    val countryList: List<CountryUiData> = listOf(),
    val status: CountryState.Status = CountryState.Status.SUCCESS,
)

fun AppState.toCountryViewState() = CountryViewState(
    countryList = countryState.countryList.asPresentation(),
    status = countryState.status
)

data class CountryUiData (
    val name: String,
    val languages: HashMap<String, String>
)
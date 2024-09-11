package com.example.reduxtestapp.ui.country

import com.example.reduxtestapp.data.model.country.CountryDto
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.state.CountryState

data class CountryViewState (
    val countryList: List<CountryItem> = listOf(),
    val status: CountryState.Status = CountryState.Status.SUCCESS,
)

fun AppState.toCountryViewState() = CountryViewState(
    countryList = countryState.countryList.asPresentation(),
    status = countryState.status
)

data class CountryItem (
    val name: String,
    val languages: Map<String, String>,
    val currencies: Map<String, String>
)

fun CountryDto.asPresentation() = CountryItem(
    name = name.common,
    languages = languages,
    currencies = currencies.mapValues { it.value.name }
)

fun List<CountryDto>.asPresentation(): List<CountryItem> = map { it.asPresentation() }
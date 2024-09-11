package com.example.reduxtestapp.redux.state

import com.example.reduxtestapp.domain.model.country.CountryModel

data class CountryState(
    val countryList: List<CountryModel> = listOf(),
    val status: Status = Status.PENDING,
) {
    enum class Status { SUCCESS, PENDING, ERROR }
}
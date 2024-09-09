package com.example.reduxtestapp.redux.state

import com.example.reduxtestapp.data.model.country.CountryDto

data class CountryState(
    val countryList: List<CountryDto> = listOf(),
    val status: Status = Status.PENDING,
) {
    enum class Status { SUCCESS, PENDING, ERROR }
}
package com.example.reduxtestapp.data.repository.country

import arrow.core.Either
import com.example.reduxtestapp.data.model.country.CountryDto
import com.example.reduxtestapp.data.network.ErrorState

interface CountryRepository {

    suspend fun getAllCountries(): Either<ErrorState, List<CountryDto>>

    suspend fun getCountries(query: String): Either<ErrorState, List<CountryDto>>

    suspend fun searchByLanguageAndCurrency(language: String, currency: String): Either<ErrorState, List<CountryDto>>

}
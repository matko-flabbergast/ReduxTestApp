package com.example.reduxtestapp.data.repository.country

import arrow.core.Either
import com.example.reduxtestapp.data.network.ErrorState
import com.example.reduxtestapp.domain.model.country.CountryModel

interface CountryRepository {

    suspend fun getAllCountries(): Either<ErrorState, List<CountryModel>>

    suspend fun getCountries(query: String): Either<ErrorState, List<CountryModel>>

    suspend fun searchByLanguageAndCurrency(language: String, currency: String): Either<ErrorState, List<CountryModel>>

    suspend fun searchByLanguageAndCurrency(language: String, currency: String): Either<ErrorState, List<CountryDto>>

}
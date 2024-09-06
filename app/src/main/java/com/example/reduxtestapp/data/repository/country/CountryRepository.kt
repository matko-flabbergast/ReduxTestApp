package com.example.reduxtestapp.data.repository.country

import arrow.core.Either
import com.example.reduxtestapp.data.model.country.Country
import com.example.reduxtestapp.data.network.ErrorState

interface CountryRepository {

    suspend fun getAllCountries(): Either<ErrorState, List<Country>>

    suspend fun getCountries(query: String): Either<ErrorState, List<Country>>

}
package com.example.reduxtestapp.data.repository.country

import android.util.Log
import arrow.core.Either
import com.example.reduxtestapp.data.model.country.Country
import com.example.reduxtestapp.data.network.CountriesApiService
import com.example.reduxtestapp.data.network.ErrorState

class CountryRepositoryImpl (
    private val countriesApi: CountriesApiService
): CountryRepository {

    override suspend fun getAllCountries(): Either<ErrorState, List<Country>> {
        return Either.catch {
            countriesApi.getAllCountries().also { Log.d("country", "getAllCountries: $it") }

        }.mapLeft { throwable ->
            ErrorState.CountriesError(throwable.message ?: "")
        }

    }

}
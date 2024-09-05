package com.example.reduxtestapp.data.repository.country

import arrow.core.Either
import arrow.core.right
import com.example.reduxtestapp.data.model.country.Country
import com.example.reduxtestapp.data.network.CountriesApiService
import com.example.reduxtestapp.data.network.ErrorState

class CountryRepositoryImpl (
    private val countriesApi: CountriesApiService
): CountryRepository {

    override suspend fun getAllCountries(): Either<ErrorState, List<Country>> {
        return Either.catch {
            countriesApi.getAllCountries()
        }.mapLeft { throwable ->
            ErrorState.CountriesError(throwable.message ?: "")
        }

    }

}
package com.example.reduxtestapp.data.repository.country

import android.util.Log
import arrow.core.Either
import com.example.reduxtestapp.data.model.country.Country
import com.example.reduxtestapp.data.network.CountriesApiService
import com.example.reduxtestapp.data.network.ErrorState
import retrofit2.HttpException

class CountryRepositoryImpl (
    private val countriesApi: CountriesApiService
): CountryRepository {

    override suspend fun getAllCountries(): Either<ErrorState, List<Country>> {
        return Either.catch {
            countriesApi.getAllCountries()
        }.mapLeft { throwable ->
            ErrorState.CountriesError(throwable.message ?: "").also {
                Log.e("country", "getAllCountries: error ${throwable.message}")
            }
        }
    }

    override suspend fun getCountries(query: String): Either<ErrorState, List<Country>> {
        return Either.catch {
            if (query.isNotEmpty()) {
                countriesApi.searchCountries(query)
            } else {
                countriesApi.getAllCountries()
            }
        }.mapLeft { throwable ->
            when (throwable) {
                is HttpException -> {
                    if (throwable.code() == 404) {
                        ErrorState.EmptyListError
                    } else {
                        ErrorState.CountriesError(throwable.message ?: "")
                    }
                }
                else -> {
                    ErrorState.CountriesError(throwable.message ?: "")
                }
            }
        }
    }

}
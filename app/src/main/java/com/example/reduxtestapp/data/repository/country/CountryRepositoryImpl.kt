package com.example.reduxtestapp.data.repository.country

import arrow.core.Either
import arrow.core.raise.either
import com.example.reduxtestapp.common.Logger
import com.example.reduxtestapp.data.model.country.CountryDto
import com.example.reduxtestapp.data.network.CountriesApiService
import com.example.reduxtestapp.data.network.ErrorState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CountryRepositoryImpl (
    private val countriesApi: CountriesApiService,
    private val logger: Logger
): CountryRepository {

    private fun logError(errorMsg: String?) {
        logger.log(
            "country",
            "getAllCountries: error $errorMsg",
            Logger.LogType.ERROR
        )
    }

    private fun handleError(throwable: Throwable): ErrorState {
        return when (throwable) {
            is HttpException -> {
                if (throwable.code() == 404) {
                    ErrorState.EmptyListError
                } else {
                    ErrorState.CountriesError(throwable.message).also {
                        logError(throwable.message)
                    }
                }
            }
            else -> {
                ErrorState.CountriesError(throwable.message).also {
                    logError(throwable.message)
                }
            }
        }
    }



    override suspend fun getAllCountries(): Either<ErrorState, List<CountryDto>> {
        return Either.catch {
            countriesApi.getAllCountries()
        }.mapLeft { throwable ->
            ErrorState.CountriesError(throwable.message).also {
                logError(throwable.message)
            }
        }
    }

    override suspend fun getCountries(query: String): Either<ErrorState, List<CountryDto>> {
        return Either.catch {
            if (query.isNotEmpty()) {
                countriesApi.searchCountries(query)
            } else {
                countriesApi.getAllCountries()
            }
        }.mapLeft { throwable ->
            handleError(throwable)
        }
    }

    override suspend fun searchByLanguageAndCurrency(
        language: String,
        currency: String
    ): Either<ErrorState, List<CountryDto>> {
        return either {

            withContext(Dispatchers.IO){

                val deferredLanguageResult = async {
                    searchByLanguage(language)
                }
                val deferredCurrencyResult = async {
                    searchByCurrency(currency)
                }

                val languageResult = deferredLanguageResult.await().bind()
                val currencyResult = deferredCurrencyResult.await().bind()
                languageResult.intersect(currencyResult.toSet()).toList()

            }

        }

    }

    private suspend fun searchByCurrency(
        currency: String
    ): Either<ErrorState,List<CountryDto>> {
        return Either.catch {
            if (currency.isNotEmpty())
                countriesApi.getByCurrency(currency)
            else
                countriesApi.getAllCountries()
        }.mapLeft { throwable ->
            handleError(throwable)
        }
    }

    private suspend fun searchByLanguage(
        language: String
    ): Either<ErrorState,List<CountryDto>> {
        return Either.catch {
            if (language.isNotEmpty())
                countriesApi.getByLanguage(language)
            else
                countriesApi.getAllCountries()
        }.mapLeft { throwable ->
            handleError(throwable)
        }
    }

}
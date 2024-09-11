package com.example.reduxtestapp.data.repository.country

import arrow.core.Either
import arrow.core.raise.either
import com.example.reduxtestapp.common.Logger
import com.example.reduxtestapp.data.model.country.asDomain
import com.example.reduxtestapp.data.cache.CacheManager
import com.example.reduxtestapp.data.model.country.CountryDto
import com.example.reduxtestapp.data.network.CountriesApiService
import com.example.reduxtestapp.data.network.ErrorState
import com.example.reduxtestapp.domain.model.country.CountryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.HttpException

const val ALL_COUNTRIES_KEY = "all_countries_key"

class CountryRepositoryImpl (
    private val countriesApi: CountriesApiService,
    private val cacheManager: CacheManager,
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



    private fun createSearchCountriesCacheKey(query: String) = "search_countries_$query"
    
    private fun createLanguageAndCurrencyCacheKey(lang: String, curr: String) = "lang_and_curr_${lang}_$curr"

    override suspend fun getAllCountries(): Either<ErrorState, List<CountryModel>> {
        return Either.catch {
            cacheManager.useCache(ALL_COUNTRIES_KEY) {
                countriesApi.getAllCountries().asDomain()
            }
        }.mapLeft { throwable ->
            ErrorState.CountriesError(throwable.message).also {
                logError(throwable.message)
            }
        }
    }

    override suspend fun getCountries(query: String): Either<ErrorState, List<CountryModel>> {
        return Either.catch {
            if (query.isNotEmpty()) {
                cacheManager.useCache(createSearchCountriesCacheKey(query)) {
                    countriesApi.searchCountries(query)
                }.asDomain()
            } else {
                cacheManager.useCache(ALL_COUNTRIES_KEY) {
                    countriesApi.getAllCountries()
                }.asDomain()
            }
        }.mapLeft { throwable ->
            handleError(throwable)
        }
    }

    override suspend fun searchByLanguageAndCurrency(
        language: String,
        currency: String
    ): Either<ErrorState, List<CountryModel>> {
        return either {

            withContext(Dispatchers.IO){
                cacheManager.useCache(createLanguageAndCurrencyCacheKey(language, currency)) {
                    val deferredLanguageResult = async {
                        searchByLanguage(language)
                    }
                    val deferredCurrencyResult = async {
                        searchByCurrency(currency)
                    }

                    val languageResult = deferredLanguageResult.await().bind()
                    val currencyResult = deferredCurrencyResult.await().bind()
                    languageResult.intersect(currencyResult.toSet()).toList().asDomain()
                }
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
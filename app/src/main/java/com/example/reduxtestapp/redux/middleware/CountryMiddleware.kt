package com.example.reduxtestapp.redux.middleware

import arrow.core.Either
import com.example.reduxtestapp.common.Logger
import com.example.reduxtestapp.data.network.ErrorState
import com.example.reduxtestapp.domain.model.country.CountryModel
import com.example.reduxtestapp.domain.use_cases.GetCountriesUseCase
import com.example.reduxtestapp.domain.use_cases.SearchCountriesUseCase
import com.example.reduxtestapp.domain.use_cases.SearchLanguageAndCurrencyUseCase
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.Middleware
import com.example.reduxtestapp.redux.MiddlewareResult

class CountryMiddleware (
    private val getCountriesUseCase: GetCountriesUseCase,
    private val searchCountriesUseCase: SearchCountriesUseCase,
    private val searchLanguageAndCurrencyUseCase: SearchLanguageAndCurrencyUseCase,
    private val logger: Logger
) : Middleware() {

    override fun handleAction(state: AppState, action: Any): MiddlewareResult {
        return when (action) {
            is Action.Country.GetCountries -> {
                launchAsyncAndFoldResult { getCountriesUseCase() }
            }
            is Action.Country.SearchCountries -> {
                launchAsyncAndFoldResult { searchCountriesUseCase(action.query) }
            }
            is Action.Country.SearchByLanguageAndCurrency -> {
                launchAsyncAndFoldResult { searchLanguageAndCurrencyUseCase(action.language, action.currency) }
            }
            is Action.Country.LoadInitialCountries -> {
                MiddlewareResult.ResultAction(Action.Country.SearchByLanguageAndCurrency("",""))
            }
            else -> MiddlewareResult.Nothing
        }

    }

    private fun launchAsyncAndFoldResult(countryListSource: suspend() -> Either<ErrorState, List<CountryModel>>): MiddlewareResult {
        return MiddlewareResult.Async{
            val networkResult = countryListSource()
            networkResult
                .fold(
                    ifRight = { countryList ->
                        MiddlewareResult.ResultAction(Action.Country.UpdateCountryList(countryList))
                    },
                    ifLeft = { error ->
                        handleError(error)
                    }
                )
        }
    }

    private fun handleError(error: ErrorState): MiddlewareResult {
        return when (error) {
            is ErrorState.EmptyListError -> {
                MiddlewareResult.ResultAction(Action.Country.UpdateCountryList(listOf()))
            }
            else -> {
                logError("countryMiddleware: error occurred $error")
                MiddlewareResult.ResultAction(Action.Country.Error())
            }
        }
    }

    private fun logError(errorMsg: String) {
        logger.log(
            "country",
            errorMsg,
            Logger.LogType.ERROR
        )
    }




}
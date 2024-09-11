package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.common.Logger
import com.example.reduxtestapp.data.network.ErrorState
import com.example.reduxtestapp.domain.use_cases.GetCountriesUseCase
import com.example.reduxtestapp.domain.use_cases.SearchCountriesUseCase
import com.example.reduxtestapp.domain.use_cases.SearchLanguageAndCurrencyUseCase
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.Middleware
import org.reduxkotlin.Store

class CountryMiddleware (
    private val getCountriesUseCase: GetCountriesUseCase,
    private val searchCountriesUseCase: SearchCountriesUseCase,
    private val searchLanguageAndCurrencyUseCase: SearchLanguageAndCurrencyUseCase,
    private val logger: Logger
) : Middleware() {

    private fun logError(errorMsg: String) {
        logger.log(
            "country",
            errorMsg,
            Logger.LogType.ERROR
        )
    }

    private fun handleError(error: ErrorState, store: Store<AppState>) {
        when (error) {
            is ErrorState.EmptyListError -> {
                store.dispatch(Action.Country.UpdateCountryList(listOf()))
            }
            else -> {
                logError("countryMiddleware: error occurred $error")
                store.dispatch(Action.Country.Error())
            }
        }
    }

    override fun middleware(store: Store<AppState>, action: Any) {

        when (action) {
            is Action.Country.GetCountries -> {
                store.dispatch(Action.Async{
                    val networkResult = getCountriesUseCase()
                    networkResult
                        .onRight { countryList ->
                            store.dispatch(Action.Country.UpdateCountryList(countryList))
                        }
                        .onLeft { error ->
                            when (error) {
                                is ErrorState.EmptyListError -> {
                                    store.dispatch(Action.Country.UpdateCountryList(listOf()))
                                }
                                else -> {
                                    logError("countryMiddleware: error occurred $error")
                                    store.dispatch(Action.Country.Error())
                                }
                            }
                        }
                })
            }
            is Action.Country.SearchCountries -> {
                store.dispatch(Action.Async{
                    val networkResult = searchCountriesUseCase(action.query)
                    networkResult
                        .onRight { countryList ->
                            store.dispatch(Action.Country.UpdateCountryList(countryList))
                        }
                        .onLeft { error ->
                            handleError(error, store)
                        }
                })
            }
            is Action.Country.SearchByLanguageAndCurrency -> {
                store.dispatch(Action.Async{
                    val networkResult = searchLanguageAndCurrencyUseCase(action.language, action.currency)
                    networkResult
                        .onRight { countryList ->
                            store.dispatch(Action.Country.UpdateCountryList(countryList))
                        }
                        .onLeft { error ->
                            handleError(error, store)
                        }
                })
            }
            is Action.Country.LoadInitialCountries -> {
                store.dispatch(Action.Country.SearchByLanguageAndCurrency("",""))
            }
        }

    }
}
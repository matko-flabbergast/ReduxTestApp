package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.common.Logger
import com.example.reduxtestapp.data.network.ErrorState
import com.example.reduxtestapp.data.repository.country.CountryRepository
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.Middleware
import org.reduxkotlin.Store

class CountryMiddleware (
    private val repo: CountryRepository,
    private val logger: Logger
) : Middleware() {

    private fun logError(errorMsg: String) {
        logger.log(
            "country",
            errorMsg,
            Logger.LogType.ERROR
        )
    }

    override fun middleware(store: Store<AppState>, action: Any) {

        when (action) {
            is Action.Country.GetCountries -> {
                store.dispatch(Action.Async{
                    val networkResult = repo.getAllCountries()
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
                    val networkResult = repo.getCountries(action.query)
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
                                    logError("countryMiddleware: error occurred error")
                                    store.dispatch(Action.Country.Error())
                                }
                            }
                        }
                })
            }
        }

    }
}
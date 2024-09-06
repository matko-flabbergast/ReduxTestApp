package com.example.reduxtestapp.redux.middleware

import android.util.Log
import com.example.reduxtestapp.data.network.ErrorState
import com.example.reduxtestapp.data.repository.country.CountryRepository
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import org.reduxkotlin.Dispatcher
import org.reduxkotlin.Store

class CountryMiddleware (
    private val repo: CountryRepository
) {
    fun countryMiddleware(store: Store<AppState>) = { next: Dispatcher ->
        { action: Any ->

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
                                    is ErrorState.CountriesError -> {
                                        Log.e("country", "countryMiddleware: error occurred ${error.message}")
                                        store.dispatch(Action.Country.Error(error.message))
                                    }
                                    is ErrorState.EmptyListError -> {
                                        store.dispatch(Action.Country.UpdateCountryList(listOf()))
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
                                    is ErrorState.CountriesError -> {
                                        Log.d("country", "countryMiddleware: error occurred ${error.message}")
                                        store.dispatch(Action.Country.Error(error.message))
                                    }
                                    is ErrorState.EmptyListError -> {
                                        store.dispatch(Action.Country.UpdateCountryList(listOf()))
                                    }
                                }
                            }
                    })
                }
            }

            val result = next(action)

            result
        }
    }
}
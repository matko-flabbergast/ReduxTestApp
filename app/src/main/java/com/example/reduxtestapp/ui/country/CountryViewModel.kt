package com.example.reduxtestapp.ui.country

import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.state.CountryState
import com.example.reduxtestapp.ui.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.reduxkotlin.Store

class CountryViewModel(
    store: Store<AppState>
) : ViewModel(store) {

    private val _uiState = MutableStateFlow(CountryViewState(
        status = CountryState.Status.PENDING
    ))

    val uiState = _uiState.asStateFlow()

    override fun onAppStateChanged(newState: AppState) {
        _uiState.value = newState.toCountryViewState()
    }

}
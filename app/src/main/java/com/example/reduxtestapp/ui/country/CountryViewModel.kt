package com.example.reduxtestapp.ui.country

import androidx.lifecycle.ViewModel
import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.state.CountryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.reduxkotlin.Store
import org.reduxkotlin.StoreSubscription

class CountryViewModel(
    private val store: Store<AppState>
) : ViewModel() {

    private var unsubscribe: StoreSubscription

    private val _uiState = MutableStateFlow(CountryViewState(
        status = CountryState.Status.PENDING
    ))
    val uiState = _uiState.asStateFlow()

    fun dispatch(action: Action) {
        store.dispatch(action)
    }

    init {
        unsubscribe = store.subscribe {
            _uiState.value = store.state.toCountryViewState()
        }
    }

    override fun onCleared() {
        unsubscribe()
        super.onCleared()
    }


}
package com.example.reduxtestapp.ui.home

import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.ui.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.reduxkotlin.Store

class HomeViewModel(
    store: Store<AppState>,
): ViewModel(store) {

    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState = _uiState.asStateFlow()

    override fun onAppStateChanged(newState: AppState) {
        _uiState.value = newState.toHomeViewState()
    }

}
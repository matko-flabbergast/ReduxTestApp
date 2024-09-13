package com.example.reduxtestapp.ui

import androidx.lifecycle.ViewModel
import com.example.reduxtestapp.redux.AppState
import org.reduxkotlin.Store
import org.reduxkotlin.StoreSubscription

abstract class ViewModel(
    private val store: Store<AppState>
) : ViewModel() {

    abstract fun onAppStateChanged(newState: AppState)

    private var unsubscribe: StoreSubscription = store.subscribe{
        onAppStateChanged(store.state)
    }

    fun dispatch(action: Any) {
        store.dispatch(action)
    }

    override fun onCleared() {
        unsubscribe()
        super.onCleared()
    }


}
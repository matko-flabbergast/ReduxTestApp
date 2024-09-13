package com.example.reduxtestapp.redux.reducers

import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.state.HomeState

fun homeReducer(state: AppState, action: Any): HomeState {
    return when (action) {
        is Action.Home.GetPrice -> {
            state.homeState.copy(
                status = HomeState.Status.PENDING
            )
        }
        is Action.Home.UpdatePrice -> {
            state.homeState.copy(
                priceModel = action.priceModel,
                status = HomeState.Status.SUCCESS
            )
        }
        else -> state.homeState
    }
}
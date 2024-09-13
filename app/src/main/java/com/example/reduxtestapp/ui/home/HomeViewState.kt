package com.example.reduxtestapp.ui.home

import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.state.HomeState
import java.text.SimpleDateFormat
import java.util.Locale

data class HomeViewState (
    val price: String = "0.0",
    val lastUpdated: String = "",
    val status: HomeState.Status = HomeState.Status.SUCCESS
)

fun AppState.toHomeViewState() = HomeViewState(
    price = String.format(Locale.getDefault(),"%.4f", homeState.priceModel.price),
    lastUpdated = SimpleDateFormat("HH:mm:ss").format(homeState.priceModel.lastUpdated),
    status = homeState.status
)
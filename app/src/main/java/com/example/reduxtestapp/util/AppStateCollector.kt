package com.example.reduxtestapp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.reduxtestapp.redux.AppState
import org.reduxkotlin.Store

@Composable
fun <T> Store<AppState>.collectState(stateMapperFunction: AppState.() -> T): T {
    var appState by remember {
        mutableStateOf(AppState())
    }
    this.subscribe {
        appState = this.state
    }
    return appState.stateMapperFunction()
}

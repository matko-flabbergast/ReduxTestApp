package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.reduxkotlin.Dispatcher
import org.reduxkotlin.Store

object AsyncMiddleware {

    fun asyncMiddleware(store: Store<AppState>) = { next: Dispatcher ->
        { action: Any ->
            if (action is Action.Async) {
                CoroutineScope(Dispatchers.IO).launch {
                    action.asyncFunc.invoke()
                }
            }
            val result = next(action)
            result
        }
    }
}
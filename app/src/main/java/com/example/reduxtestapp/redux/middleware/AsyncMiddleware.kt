package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.Middleware
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.reduxkotlin.Store

class AsyncMiddleware : Middleware(){

    override fun middleware(store: Store<AppState>, action: Any) {
        if (action is Action.Async) {
            CoroutineScope(Dispatchers.IO).launch {
                action.asyncFunc.invoke()
            }
        }
    }
}
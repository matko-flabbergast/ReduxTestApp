package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.redux.Action
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.Middleware
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.reduxkotlin.Store

class AsyncMiddleware(
    private val dispatcher: CoroutineDispatcher
) : Middleware(){

    override fun middleware(store: Store<AppState>, action: Any) {
        if (action is Action.Async) {
            CoroutineScope(dispatcher).launch {
                action.asyncFunc.invoke()
            }
        }
    }
}
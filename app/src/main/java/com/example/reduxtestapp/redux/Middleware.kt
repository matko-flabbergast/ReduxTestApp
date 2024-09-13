package com.example.reduxtestapp.redux

import org.reduxkotlin.Dispatcher
import org.reduxkotlin.Store

abstract class Middleware {

    abstract fun middleware(store: Store<AppState>, action: Any)

    fun launchMiddleware(store: Store<AppState>) = { next: Dispatcher ->
        { action: Any ->
            val result = next(action)
            middleware(store, action)
            result
        }
    }
}
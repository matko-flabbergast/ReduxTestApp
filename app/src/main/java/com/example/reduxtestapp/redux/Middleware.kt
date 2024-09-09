package com.example.reduxtestapp.redux

import org.reduxkotlin.Dispatcher
import org.reduxkotlin.Store

abstract class Middleware {

    abstract fun middleware(store: Store<AppState>, action: Any)

    fun launchMiddleware(store: Store<AppState>) = { next: Dispatcher ->
        { action: Any ->
            middleware(store, action)
            val result = next(action)
            result
        }
    }
}
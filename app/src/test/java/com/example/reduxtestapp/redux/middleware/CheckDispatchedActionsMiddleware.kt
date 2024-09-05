package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.redux.AppState
import org.reduxkotlin.Dispatcher
import org.reduxkotlin.Store

object CheckDispatchedActionsMiddleware {

    val dispatchedActions = mutableListOf<Any>()

    fun actionsMiddleware(store: Store<AppState>) = { next: Dispatcher ->
        { action: Any ->

            dispatchedActions.add(action)

            next(action)

        }
    }
}
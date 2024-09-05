package com.example.reduxtestapp.redux.middleware

import com.example.reduxtestapp.redux.AppState
import org.reduxkotlin.Dispatcher
import org.reduxkotlin.Store

object CheckDispatchedActionsMiddleware {

    private val _dispatchedActions = mutableListOf<Any>()
    val dispatchedActions: List<Any> = _dispatchedActions


    fun actionsMiddleware(store: Store<AppState>) = { next: Dispatcher ->
        { action: Any ->

            _dispatchedActions.add(action)

            next(action)

        }
    }
}
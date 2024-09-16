package com.example.reduxtestapp.redux


abstract class Middleware {

    abstract fun handleAction(state: AppState, action: Any): MiddlewareResult

}
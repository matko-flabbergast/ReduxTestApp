package com.example.reduxtestapp.redux.di

import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.middleware.AsyncMiddleware
import com.example.reduxtestapp.redux.middleware.CountryMiddleware
import com.example.reduxtestapp.redux.middleware.TodoMiddleware
import com.example.reduxtestapp.redux.rootReducer
import org.koin.dsl.module
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createThreadSafeStore

val reduxModule = module {
    single<Store<AppState>>{
        createThreadSafeStore(
            reducer = { state, action ->
                rootReducer(state, action)
            },
            preloadedState = AppState(),
            enhancer = applyMiddleware(
                get<TodoMiddleware>()::launchMiddleware,
                get<CountryMiddleware>()::launchMiddleware,
                get<AsyncMiddleware>()::launchMiddleware
            )
        )
    }

    single<TodoMiddleware> {
        TodoMiddleware(
            get()
        )
    }

    single<CountryMiddleware>{
        CountryMiddleware(
            get(),
            get(),
            get(),
            get()
        )
    }

    single<AsyncMiddleware>{
        AsyncMiddleware()
    }
}
package com.example.reduxtestapp.redux.di

import com.example.reduxtestapp.common.di.BACKGROUND_DISPATCHER
import com.example.reduxtestapp.common.di.IO_DISPATCHER
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.middleware.AsyncMiddleware
import com.example.reduxtestapp.redux.middleware.CountryMiddleware
import com.example.reduxtestapp.redux.middleware.PriceMiddleware
import com.example.reduxtestapp.redux.middleware.StreamMiddleware
import com.example.reduxtestapp.redux.middleware.TodoMiddleware
import com.example.reduxtestapp.redux.rootReducer
import org.koin.core.qualifier.named
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
                get<PriceMiddleware>()::launchMiddleware,
                get<AsyncMiddleware>()::launchMiddleware,
                get<StreamMiddleware>()::launchMiddleware,
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

    single<PriceMiddleware>{
        PriceMiddleware(
            get(), get()
        )
    }

    single<AsyncMiddleware>{
        AsyncMiddleware(
            get(named(IO_DISPATCHER))
        )
    }

    single<StreamMiddleware>{
        StreamMiddleware(
            get(named(BACKGROUND_DISPATCHER))
        )
    }
}
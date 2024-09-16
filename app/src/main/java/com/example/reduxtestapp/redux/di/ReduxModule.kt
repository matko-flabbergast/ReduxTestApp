package com.example.reduxtestapp.redux.di

import com.example.reduxtestapp.common.di.IO_DISPATCHER
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.middleware.CountryMiddleware
import com.example.reduxtestapp.redux.middleware.PriceMiddleware
import com.example.reduxtestapp.redux.middleware.RootMiddleware
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
                get<RootMiddleware>()::launchMiddleware
            )
        )
    }

    single<RootMiddleware> {
        RootMiddleware(get(named(IO_DISPATCHER)), listOf(
            get<TodoMiddleware>(),
            get<CountryMiddleware>(),
            get<PriceMiddleware>()
        ))
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

}
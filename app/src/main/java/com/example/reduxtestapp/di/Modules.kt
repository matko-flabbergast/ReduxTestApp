package com.example.reduxtestapp.di

import com.example.reduxtestapp.data.network.BASE_URL
import com.example.reduxtestapp.data.network.CountriesApiService
import com.example.reduxtestapp.data.repository.country.CountryRepository
import com.example.reduxtestapp.data.repository.country.CountryRepositoryImpl
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.middleware.TodoMiddleware
import com.example.reduxtestapp.data.repository.todo.TodoRepository
import com.example.reduxtestapp.data.repository.todo.TodoRepositoryImpl
import com.example.reduxtestapp.redux.middleware.AsyncMiddleware
import com.example.reduxtestapp.redux.middleware.CountryMiddleware
import com.example.reduxtestapp.redux.rootReducer
import org.koin.dsl.module
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createThreadSafeStore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val sharedModule = module {

    single<TodoRepository>{
        TodoRepositoryImpl(get())
    }

    single<CountryRepository>{
        CountryRepositoryImpl(get())
    }

    single<Store<AppState>>{
        createThreadSafeStore(
            reducer = { state, action ->
                rootReducer(state, action)
            },
            preloadedState = AppState(),
            enhancer = applyMiddleware(
                get<TodoMiddleware>()::todoMiddleware,
                get<CountryMiddleware>()::countryMiddleware,
                get<AsyncMiddleware>()::asyncMiddleware
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
            get()
        )
    }

    single<AsyncMiddleware>{
        AsyncMiddleware()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<CountriesApiService> {
        get<Retrofit>().create(CountriesApiService::class.java)
    }
}
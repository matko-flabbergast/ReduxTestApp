package com.example.reduxtestapp.di

import com.example.reduxtestapp.data.network.BASE_URL
import com.example.reduxtestapp.data.network.CountriesApiService
import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.middleware.RepoMiddleware
import com.example.reduxtestapp.data.repository.todo.TodoRepository
import com.example.reduxtestapp.data.repository.todo.TodoRepositoryImplementation
import com.example.reduxtestapp.redux.middleware.AsyncMiddleware
import com.example.reduxtestapp.redux.todosReducer
import org.koin.dsl.module
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createThreadSafeStore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val sharedModule = module {

    single<TodoRepository>{
        TodoRepositoryImplementation(get())
    }

    single<Store<AppState>>{
        createThreadSafeStore(
            reducer = { state, action ->
                todosReducer(state, action)
            },
            preloadedState = AppState(),
            enhancer = applyMiddleware(
                get<RepoMiddleware>()::todoMiddleware,
                get<AsyncMiddleware>()::asyncMiddleware
            )
        )
    }

    single<RepoMiddleware> {
        RepoMiddleware(
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
package com.example.reduxtestapp.di

import com.example.reduxtestapp.redux.AppState
import com.example.reduxtestapp.redux.middleware.RepoMiddleware
import com.example.reduxtestapp.data.repository.TodoRepository
import com.example.reduxtestapp.data.repository.TodoRepositoryImplementation
import com.example.reduxtestapp.redux.middleware.AsyncMiddleware
import com.example.reduxtestapp.redux.todosReducer
import org.koin.dsl.module
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createThreadSafeStore

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
                AsyncMiddleware::asyncMiddleware
            )
        )
    }

    single<RepoMiddleware> {
        RepoMiddleware(
            get()
        )
    }
}
package com.example.reduxtestapp.data.di

import com.example.reduxtestapp.data.repository.todo.TodoRepository
import com.example.reduxtestapp.data.repository.todo.TodoRepositoryImpl
import org.koin.dsl.module

val todoDataModule = module {
    single<TodoRepository>{
        TodoRepositoryImpl(get())
    }
}
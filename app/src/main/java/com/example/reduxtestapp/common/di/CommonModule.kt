package com.example.reduxtestapp.common.di

import com.example.reduxtestapp.common.DefaultLogger
import com.example.reduxtestapp.common.Logger
import org.koin.dsl.module

val commonModule = module {
    single<Logger> {
        DefaultLogger
    }
}
package com.example.reduxtestapp.common.di

import com.example.reduxtestapp.common.DefaultLogger
import com.example.reduxtestapp.common.Logger
import com.example.reduxtestapp.common.Poller
import com.example.reduxtestapp.common.PollerImpl
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val IO_DISPATCHER = "IODispatcher"
const val BACKGROUND_DISPATCHER = "BackgroundDispatcher"
const val MAIN_DISPATCHER = "MainDispatcher"

val commonModule = module {
    single<Logger> {
        DefaultLogger
    }

    single<Poller> {
        PollerImpl()
    }

    single(named(IO_DISPATCHER)) {
        Dispatchers.IO
    }
    single(named(BACKGROUND_DISPATCHER)) {
        Dispatchers.Default
    }
    single(named(MAIN_DISPATCHER)) {
        Dispatchers.Main
    }
}
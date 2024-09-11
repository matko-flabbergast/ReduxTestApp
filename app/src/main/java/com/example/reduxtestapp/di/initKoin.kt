package com.example.reduxtestapp.di

import com.example.reduxtestapp.common.di.commonModule
import com.example.reduxtestapp.data.di.countryDataModule
import com.example.reduxtestapp.data.di.dataModule
import com.example.reduxtestapp.data.di.todoDataModule
import com.example.reduxtestapp.domain.di.domainModule
import com.example.reduxtestapp.redux.di.reduxModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            commonModule,
            dataModule,
            todoDataModule,
            countryDataModule,
            reduxModule,
            domainModule
        )
    }
}

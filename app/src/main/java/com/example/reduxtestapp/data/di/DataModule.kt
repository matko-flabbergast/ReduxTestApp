package com.example.reduxtestapp.data.di

import com.example.reduxtestapp.data.cache.CacheManager
import com.example.reduxtestapp.data.cache.CacheManagerImpl
import org.koin.dsl.module

val dataModule = module {
    single<CacheManager> {
        CacheManagerImpl()
    }
    includes(countryDataModule, todoDataModule, priceDataModule)
}
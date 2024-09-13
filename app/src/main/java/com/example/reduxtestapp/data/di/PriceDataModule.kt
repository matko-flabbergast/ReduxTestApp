package com.example.reduxtestapp.data.di

import com.example.reduxtestapp.data.repository.price.PriceRepository
import com.example.reduxtestapp.data.repository.price.PriceRepositoryImpl
import org.koin.dsl.module

val priceDataModule = module {
    single<PriceRepository> {
        PriceRepositoryImpl()
    }
}
package com.example.reduxtestapp.data.di

import com.example.reduxtestapp.data.network.BASE_URL
import com.example.reduxtestapp.data.network.CountriesApiService
import com.example.reduxtestapp.data.repository.country.CountryRepository
import com.example.reduxtestapp.data.repository.country.CountryRepositoryImpl
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val countryDataModule = module {

    single<CountryRepository>{
        CountryRepositoryImpl(get(), get(), get())
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
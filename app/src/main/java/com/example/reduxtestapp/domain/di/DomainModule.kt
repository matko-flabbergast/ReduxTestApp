package com.example.reduxtestapp.domain.di

import com.example.reduxtestapp.domain.use_cases.GetCountriesUseCase
import com.example.reduxtestapp.domain.use_cases.SearchCountriesUseCase
import com.example.reduxtestapp.domain.use_cases.SearchLanguageAndCurrencyUseCase
import org.koin.dsl.module

val domainModule = module {
    single<GetCountriesUseCase> {
        GetCountriesUseCase(get())
    }

    single<SearchCountriesUseCase> {
        SearchCountriesUseCase(get())
    }

    single<SearchLanguageAndCurrencyUseCase> {
        SearchLanguageAndCurrencyUseCase(get())
    }
}
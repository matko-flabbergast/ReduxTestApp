package com.example.reduxtestapp.ui.country.di

import com.example.reduxtestapp.ui.country.CountryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val countryUiModule = module {
    viewModel {
        CountryViewModel(get())
    }
}
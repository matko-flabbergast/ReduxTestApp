package com.example.reduxtestapp.ui.di

import com.example.reduxtestapp.ui.country.di.countryUiModule
import com.example.reduxtestapp.ui.home.di.homeUiModule
import org.koin.dsl.module

val uiModule = module {
    includes(countryUiModule, homeUiModule)
}
package com.example.reduxtestapp.ui.di

import com.example.reduxtestapp.ui.country.di.countryUiModule
import org.koin.dsl.module

val uiModule = module {
    includes(countryUiModule)
}
package com.example.reduxtestapp.ui.home.di

import com.example.reduxtestapp.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeUiModule = module{
    viewModel {
        HomeViewModel(get())
    }
}
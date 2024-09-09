package com.example.reduxtestapp.data.network

sealed class ErrorState{
    data class CountriesError(val message: String?) : ErrorState()
    data object EmptyListError : ErrorState()
}
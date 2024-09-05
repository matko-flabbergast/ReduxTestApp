package com.example.reduxtestapp.data.network

import arrow.core.Either
import com.example.reduxtestapp.data.model.country.Country
import retrofit2.http.GET

const val BASE_URL = "https://restcountries.com/v3.1/"

interface CountriesApiService {

    @GET("all")
    fun getAllCountries() : List<Country>

}
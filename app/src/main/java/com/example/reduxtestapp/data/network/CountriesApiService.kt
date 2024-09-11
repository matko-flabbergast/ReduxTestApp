package com.example.reduxtestapp.data.network

import com.example.reduxtestapp.data.model.country.CountryDto
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://restcountries.com/v3.1/"

interface CountriesApiService {

    @GET("all")
    suspend fun getAllCountries() : List<CountryDto>

    @GET("name/{query}")
    suspend fun searchCountries(@Path("query") query: String) : List<CountryDto>

    @GET("currency/{currency}")
    suspend fun getByCurrency(@Path("currency") currency: String): List<CountryDto>

    @GET("lang/{language}")
    suspend fun getByLanguage(@Path("language") language: String): List<CountryDto>
}
package com.example.reduxtestapp.data.model.country

import com.google.gson.annotations.SerializedName

data class CountryDto(
    val name: Name = Name(),
    @SerializedName("capital")
    val capitals: List<String> = listOf(),
    val currencies: Map<String, Currency> = hashMapOf(),
    val languages: Map<String, String> = hashMapOf(),
    val population: Int = 0,
    val flags: Map<String, String> = hashMapOf(),
    val translations: Map<String, Name> = hashMapOf()
)

data class Name (
    val common: String = "",
    val official: String = ""
)

data class Currency (
    val name: String,
    val symbol: String
)




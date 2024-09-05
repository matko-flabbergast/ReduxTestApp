package com.example.reduxtestapp.data.model.country

import com.google.gson.annotations.SerializedName

data class Country(
    val name: Name,
    @SerializedName("capital")
    val capitals: List<String>,
    val currencies: HashMap<String, Currency>,
    val languages: HashMap<String, String>,
    val population: Int,
    val flags: HashMap<String, String>,
    val translations: HashMap<String, Name>
)

data class Name (
    val common: String,
    val official: String
)

data class Currency (
    val name: String,
    val symbol: String
)


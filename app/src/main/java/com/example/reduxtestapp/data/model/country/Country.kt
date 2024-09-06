package com.example.reduxtestapp.data.model.country

import com.example.reduxtestapp.ui.country.CountryUiData
import com.google.gson.annotations.SerializedName

data class Country(
    val name: Name = Name(),
    @SerializedName("capital")
    val capitals: List<String> = listOf(),
    val currencies: HashMap<String, Currency> = hashMapOf(),
    val languages: HashMap<String, String> = hashMapOf(),
    val population: Int = 0,
    val flags: HashMap<String, String> = hashMapOf(),
    val translations: HashMap<String, Name> = hashMapOf()
)

data class Name (
    val common: String = "",
    val official: String = ""
)

data class Currency (
    val name: String,
    val symbol: String
)


fun Country.asPresentation() = CountryUiData(
    name = name.common,
    languages = languages
)

fun List<Country>.asPresentation(): List<CountryUiData> = map { it.asPresentation() }


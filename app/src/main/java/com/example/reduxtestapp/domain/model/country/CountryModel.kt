package com.example.reduxtestapp.domain.model.country


data class CountryModel(
    val name: NameModel = NameModel(),
    val capitals: List<String> = listOf(),
    val currencies: Map<String, CurrencyModel> = hashMapOf(),
    val languages: Map<String, String> = hashMapOf(),
    val population: Int = 0,
    val flags: Map<String, String> = hashMapOf(),
    val translations: Map<String, NameModel> = hashMapOf()
)

data class NameModel (
    val common: String = "",
    val official: String = ""
)

data class CurrencyModel (
    val name: String,
    val symbol: String
)
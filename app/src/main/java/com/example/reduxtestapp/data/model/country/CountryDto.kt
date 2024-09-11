package com.example.reduxtestapp.data.model.country

import com.example.reduxtestapp.domain.model.country.CountryModel
import com.example.reduxtestapp.domain.model.country.CurrencyModel
import com.example.reduxtestapp.domain.model.country.NameModel
import com.google.gson.annotations.SerializedName

data class CountryDto(
    val name: NameDto = NameDto(),
    @SerializedName("capital")
    val capitals: List<String> = listOf(),
    val currencies: Map<String, CurrencyDto> = hashMapOf(),
    val languages: Map<String, String> = hashMapOf(),
    val population: Int = 0,
    val flags: Map<String, String> = hashMapOf(),
    val translations: Map<String, NameDto> = hashMapOf()
)

data class NameDto (
    val common: String = "",
    val official: String = ""
)

data class CurrencyDto (
    val name: String,
    val symbol: String
)

fun NameDto.asDomain() = NameModel(
    common = common,
    official = official
)

fun CurrencyDto.asDomain() = CurrencyModel(
    name = name,
    symbol = symbol
)



fun CountryDto.asDomain() = CountryModel(
    name = name.asDomain(),
    capitals = capitals,
    currencies = currencies.mapValues { it.value.asDomain() },
    languages = languages,
    population = population,
    flags = flags,
    translations = translations.mapValues { it.value.asDomain() }
)

fun List<CountryDto>.asDomain() = map { it.asDomain() }




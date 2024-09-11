package com.example.reduxtestapp.domain.use_cases

import com.example.reduxtestapp.data.repository.country.CountryRepository

class SearchLanguageAndCurrencyUseCase (
    private val repo: CountryRepository
) {

    suspend operator fun invoke(lang: String, curr: String) = repo.searchByLanguageAndCurrency(lang, curr)
}
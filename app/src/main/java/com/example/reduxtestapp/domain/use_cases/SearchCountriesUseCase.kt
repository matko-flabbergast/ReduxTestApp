package com.example.reduxtestapp.domain.use_cases

import com.example.reduxtestapp.data.repository.country.CountryRepository

class SearchCountriesUseCase (
    private val repo: CountryRepository
){

    suspend operator fun invoke(query: String) = repo.getCountries(query)
}
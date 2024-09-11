package com.example.reduxtestapp.domain.use_cases

import arrow.core.Either
import com.example.reduxtestapp.data.network.ErrorState
import com.example.reduxtestapp.data.repository.country.CountryRepository
import com.example.reduxtestapp.domain.model.country.CountryModel

class GetCountriesUseCase (
    private val repo: CountryRepository,
){

    suspend operator fun invoke(): Either<ErrorState, List<CountryModel>> {

        return repo.getAllCountries()
    }
}
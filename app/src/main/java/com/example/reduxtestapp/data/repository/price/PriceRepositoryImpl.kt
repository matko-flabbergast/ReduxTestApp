package com.example.reduxtestapp.data.repository.price

import com.example.reduxtestapp.data.model.price.PriceDto
import com.example.reduxtestapp.data.model.price.asDomain
import com.example.reduxtestapp.domain.model.price.PriceModel
import kotlinx.coroutines.delay
import java.util.Date
import kotlin.random.Random

class PriceRepositoryImpl : PriceRepository {
    override suspend fun getPrice(): PriceModel {
        delay(500L)
        return PriceDto(
            Random.nextDouble(0.0,10.0),
            Date()
        ).asDomain()
    }
}
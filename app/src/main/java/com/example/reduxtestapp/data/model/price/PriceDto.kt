package com.example.reduxtestapp.data.model.price

import com.example.reduxtestapp.domain.model.price.PriceModel
import java.util.Date

data class PriceDto (
    val price: Double = 0.0,
    val lastUpdated: Date = Date()
)

fun PriceDto.asDomain() = PriceModel(
    price = price,
    lastUpdated = lastUpdated
)
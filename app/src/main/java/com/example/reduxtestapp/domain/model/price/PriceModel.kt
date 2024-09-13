package com.example.reduxtestapp.domain.model.price

import java.util.Date

data class PriceModel (
    val price: Double = 0.0,
    val lastUpdated: Date = Date()
)
package com.example.reduxtestapp.data.repository.price

import com.example.reduxtestapp.domain.model.price.PriceModel

interface PriceRepository {

    suspend fun getPrice(): PriceModel
}
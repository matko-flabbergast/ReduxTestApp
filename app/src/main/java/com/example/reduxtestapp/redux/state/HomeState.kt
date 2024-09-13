package com.example.reduxtestapp.redux.state

import com.example.reduxtestapp.domain.model.price.PriceModel

data class HomeState (
    val priceModel: PriceModel = PriceModel(),
    val status: Status = Status.PENDING,
) {
    enum class Status { SUCCESS, PENDING, ERROR }
}
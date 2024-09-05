package com.example.reduxtestapp.util

import arrow.core.Either
import arrow.core.raise.either
import com.example.reduxtestapp.data.network.ErrorState

class NetworkExtensions {
    suspend fun <T> eitherResponse(func: suspend () -> T): Either<ErrorState, T> {
        return either{
            func.invoke()
        }



    }
}
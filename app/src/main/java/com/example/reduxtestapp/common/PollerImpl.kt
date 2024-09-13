package com.example.reduxtestapp.common

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PollerImpl: Poller {
    override fun <T> poll(intervalMillis: Long, block: suspend () -> T): Flow<T> {
        return flow {
            while (true) {
                emit(block())
                delay(intervalMillis)
            }
        }
    }
}
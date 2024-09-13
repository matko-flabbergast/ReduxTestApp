package com.example.reduxtestapp.common

import kotlinx.coroutines.flow.Flow

interface Poller {
    fun <T> poll(intervalMillis: Long, block: suspend () -> T): Flow<T>
}
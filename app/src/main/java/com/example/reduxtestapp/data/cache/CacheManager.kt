package com.example.reduxtestapp.data.cache

interface CacheManager {

    fun get(key: String): Any?

    fun put(key: String, value: Any)

    suspend fun <T> useCache(key: String, valueSource: suspend () -> T): T
}
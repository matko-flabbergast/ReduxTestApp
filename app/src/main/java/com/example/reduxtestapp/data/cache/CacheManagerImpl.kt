package com.example.reduxtestapp.data.cache

import java.util.concurrent.ConcurrentHashMap

class CacheManagerImpl : CacheManager {

    private val cache = ConcurrentHashMap<String, CacheEntry>()

    private val cacheEntryExpiresMillis = 10 * 60 * 1000L

    override fun get(key: String): Any? {
        val entry = cache[key]
        if (entry == null || entry.isExpired()) {
            return null
        }
        return entry.value
    }

    override fun put(key: String, value: Any) {
        cache[key] = CacheEntry(value, cacheEntryExpiresMillis)
    }

    override suspend fun <T> useCache(key: String, valueSource: suspend () -> T): T {
        val cachedValue = get(key) as? T
        return cachedValue ?: valueSource().also { put(key, it as Any) }
    }

    data class CacheEntry(
        val value: Any,
        val expirationDurationMillis: Long
    ) {
        private val creationTime = now()
        private fun now() = System.currentTimeMillis()
        fun isExpired() = (now() - creationTime) > expirationDurationMillis
    }




}
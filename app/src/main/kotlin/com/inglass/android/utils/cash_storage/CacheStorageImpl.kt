package com.inglass.android.utils.cash_storage

import androidx.collection.LruCache

class CacheStorageImpl : CacheStorage {

    private companion object {
        const val LRU_CACHE_MAX_SIZE = 16
        const val MAX_TIME_IN_CASH = 5_000_000L
    }

    private val lruCache: LruCache<String, CacheEntry<*>> = LruCache(LRU_CACHE_MAX_SIZE)

    override fun <T> get(key: String): T? {
        val cacheItem = lruCache.get(key) ?: return null

        val isExpired = System.currentTimeMillis() - cacheItem.createdAt > MAX_TIME_IN_CASH
        return if (isExpired) {
            lruCache.remove(key)
            null
        } else {
            @Suppress("UNCHECKED_CAST")
            cacheItem.value as T
        }
    }

    override fun <T> set(key: String, value: T) {
        lruCache.put(key, CacheEntry(value))
    }

    override fun clear(key: String) {
        lruCache.remove(key)
    }

    override fun clearAll() {
        lruCache.evictAll()
    }
}

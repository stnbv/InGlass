package com.inglass.android.utils.cash_storage

interface CacheStorage {
    fun <T> set(key: String, value: T)
    fun <T> get(key: String): T?
    fun clear(key: String)
    fun clearAll()
}

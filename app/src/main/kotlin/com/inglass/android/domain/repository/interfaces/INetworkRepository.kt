package com.inglass.android.domain.repository.interfaces

interface INetworkRepository {
    fun isNetworkAvailable(): Boolean
    suspend fun waitAvailableNetwork()
}

package com.inglass.android.domain.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.getSystemService
import com.inglass.android.domain.repository.interfaces.INetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

class NetworkRepository(context: Context) : INetworkRepository {

    private val availabilityFlow = MutableStateFlow(false)

    init {
        val manager = context.getSystemService<ConnectivityManager>()!!
        val request = createNetworkRequest()
        val callback = createNetworkCallback()
        // Android will unregister callback when app killed
        manager.registerNetworkCallback(request, callback)
    }

    private fun createNetworkRequest() = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        .build()

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        // NetworkCallback with request receives onAvailable for each network satisfying given request
        private var count = 0

        override fun onAvailable(network: Network) {
            count++
            availabilityFlow.tryEmit(true)
        }

        override fun onLost(network: Network) {
            count--
            availabilityFlow.tryEmit(count != 0)
        }
    }

    override fun isNetworkAvailable() = availabilityFlow.value

    override suspend fun waitAvailableNetwork() {
        availabilityFlow.first { it }
    }
}

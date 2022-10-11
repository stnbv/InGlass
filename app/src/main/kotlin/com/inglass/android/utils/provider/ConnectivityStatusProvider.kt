package com.inglass.android.utils.provider

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inglass.android.utils.provider.ConnectivityStatusProvider.ConnectivityStatus.CONNECTED
import com.inglass.android.utils.provider.ConnectivityStatusProvider.ConnectivityStatus.DISCONNECTED

class ConnectivityStatusProvider(context: Context) {

    private val connectionStatus = MutableLiveData<ConnectivityStatus>()

    val connectionStatusLiveData: LiveData<ConnectivityStatus>
        get() = connectionStatus

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            connectionStatus.postValue(CONNECTED)
        }

        override fun onLost(network: Network) {
            connectionStatus.postValue(DISCONNECTED)
        }
    }

    init {
        context.getSystemService(ConnectivityManager::class.java)?.registerDefaultNetworkCallback(networkCallback)
    }

    enum class ConnectivityStatus {
        CONNECTED,
        DISCONNECTED
    }
}

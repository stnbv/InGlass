package com.inglass.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.provider.ConnectivityStatusProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppActivityVM @Inject constructor(
    private val connectivityStatusProvider: ConnectivityStatusProvider,
    private val prefs: IPreferencesRepository
) : BaseViewModel() {

    val showMenu = MutableLiveData(false)

    val connectivityLiveData: LiveData<ConnectivityStatusProvider.ConnectivityStatus>
        get() = connectivityStatusProvider.connectionStatusLiveData

}

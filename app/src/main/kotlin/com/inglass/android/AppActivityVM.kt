package com.inglass.android

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.presentation.CameraXLivePreviewActivity
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.provider.ConnectivityStatusProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AppActivityVM @Inject constructor(
    private val connectivityStatusProvider: ConnectivityStatusProvider,
    private val prefs: IPreferencesRepository
) : BaseViewModel() {

    val showMenu = MutableLiveData(false)

    val connectivityLiveData: LiveData<ConnectivityStatusProvider.ConnectivityStatus>
        get() = connectivityStatusProvider.connectionStatusLiveData

}

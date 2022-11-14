package com.inglass.android.presentation.settings

import androidx.lifecycle.MutableLiveData
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.navigation.SCREENS
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.URL
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class SettingsVM @Inject constructor(
    private val preferencesRepository: IPreferencesRepository
) : BaseViewModel() {

    val baseUrl = MutableLiveData("")

    fun checkFields() {
        return try {
            preferencesRepository.baseUrl = URL(baseUrl.value).toString().removeSuffix("/")
            navigateToScreen(SCREENS.LOGIN)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }
}

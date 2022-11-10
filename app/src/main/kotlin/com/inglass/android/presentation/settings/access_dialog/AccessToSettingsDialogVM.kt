package com.inglass.android.presentation.settings.access_dialog

import androidx.lifecycle.MutableLiveData
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.navigation.SCREENS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val CURRENT_PASSWORD = "11111" //пароль для входа в настройки приложения

@HiltViewModel
class AccessToSettingsDialogVM @Inject constructor() : BaseViewModel() {
    var passwordText = MutableLiveData("")
    var passwordError = MutableLiveData(false)

    fun checkPassword() {
        if (passwordText.value == CURRENT_PASSWORD) navigateToScreen(SCREENS.SETTINGS)
        else passwordError.value = true
    }
}

package com.inglass.android.presentation.auth_screens.settings

import androidx.lifecycle.MutableLiveData
import com.inglass.android.R
import com.inglass.android.utils.ErrorWrapper
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.errors.conditions.NetworkCondition
import com.inglass.android.utils.errors.conditions.PhoneByMaskCondition
import com.inglass.android.utils.errors.observers.DefaultObserverValidate
import com.inglass.android.utils.errors.validators.Validator
import com.inglass.android.utils.navigation.SCREENS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor() :
    BaseViewModel() {

    val phone = MutableLiveData("")
    val phoneErrorWrapper = MutableLiveData<ErrorWrapper>(ErrorWrapper.None)
    val countryCode = MutableLiveData<String>()
    val mask = MutableLiveData<String>()
    private val phoneCondition = PhoneByMaskCondition(MutableLiveData(""), R.string.error_phone_number)
    private val phoneValidator = Validator<String>(phone).apply {
        addCondition(phoneCondition)
        setObserver(DefaultObserverValidate(phoneErrorWrapper))
    }

    private val networkError = MutableLiveData<Answer.Failure?>(null)
    private val networkValidator = Validator(networkError).apply {
        addCondition(NetworkCondition(useServerMessage = true))
        setObserver(DefaultObserverValidate(phoneErrorWrapper))
    }

     fun navToLogin() {
        navigateToScreen(SCREENS.LOGIN)
    }

    private fun checkFields(): Boolean {
        phoneValidator.validation()
        return phoneValidator.getState() == ErrorWrapper.None
    }
}

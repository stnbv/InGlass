package com.inglass.android.presentation.auth_screens.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inglass.android.R
import com.inglass.android.domain.models.Country
import com.inglass.android.domain.models.Country.NO_COUNTRY
import com.inglass.android.domain.usecase.auth.LogInUseCase
import com.inglass.android.domain.usecase.auth.LogInUseCase.Params
import com.inglass.android.utils.ErrorWrapper
import com.inglass.android.utils.analitics.AnalyticMessage.AnalyticsLogInEvent
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.ErrorCode
import com.inglass.android.utils.api.core.onFailure
import com.inglass.android.utils.api.core.onSuccess
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.errors.conditions.NetworkConditionMessagesLogin
import com.inglass.android.utils.errors.conditions.PassCondition
import com.inglass.android.utils.errors.conditions.PhoneByMaskCondition
import com.inglass.android.utils.errors.observers.DefaultObserverValidate
import com.inglass.android.utils.errors.operators.DefaultMuxOperator
import com.inglass.android.utils.errors.validators.MuxValidator
import com.inglass.android.utils.errors.validators.Validator
import com.inglass.android.utils.helpers.getCountryByName
import com.inglass.android.utils.helpers.getPhone
import com.inglass.android.utils.navigation.DIALOGS.ACCESS_TO_SETTINGS
import com.inglass.android.utils.navigation.SCREENS
import com.inglass.android.utils.navigation.SCREENS.DESKTOP
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginVM @Inject constructor(private val logInUseCase: LogInUseCase) : BaseViewModel() {

    val prefsCountry = MutableLiveData<Country>()
    val showLoader = MutableLiveData(false)
    val phone = MutableLiveData("")
    val country = MutableLiveData(NO_COUNTRY)
    val phoneErrorWrapper = MutableLiveData<ErrorWrapper>(ErrorWrapper.None)
    val loginButtonVisibility = MutableLiveData(true)
    val mask = MutableLiveData<String>()
    val countryCode = MutableLiveData<String>()
    private val phoneCondition = PhoneByMaskCondition(MutableLiveData(""))

    val password = MutableLiveData("")
    val passErrorWrapper = MutableLiveData<ErrorWrapper>(ErrorWrapper.None)

    val currentError = MutableLiveData<ErrorWrapper>(ErrorWrapper.None)
    private val networkError = MutableLiveData<Answer.Failure?>(null)

    private val phoneValidator = Validator<String>(phone).apply {
        addCondition(phoneCondition)
        setObserver(DefaultObserverValidate(phoneErrorWrapper))
    }

    private val passValidator = Validator<String>(password).apply {
        addCondition(PassCondition())
        setObserver(DefaultObserverValidate(passErrorWrapper))
    }

    private val muxValidator = MuxValidator(DefaultMuxOperator(R.string.error_login_or_pass)).apply {
        addValidator(phoneValidator)
        addValidator(passValidator)
        setObserver(DefaultObserverValidate(currentError))
    }

    private val networkConnectionValidator = Validator(networkError).apply {
        addCondition(
            NetworkConditionMessagesLogin(
                mapOf(ErrorCode.InternalError to R.string.error_internal_connectivity_error),
                useServerMessage = true
            )
        )
        setObserver(DefaultObserverValidate(currentError))
    }

    fun setCountryMask(selectedCountry: String, selectedCountryCode: String, phoneMask: String) {
        mask.value = phoneMask
        countryCode.value = selectedCountryCode
        country.value = getCountryByName(selectedCountry)
        phoneCondition.mask = mask
    }

    fun onLoginClick() {
        navigateToScreen(DESKTOP)
//        viewModelScope.launch {
//            if (checkFields()) {
//                val password = password.value ?: return@launch
//                showLoader.value = true
//                logInUseCase(Params(getPhone(countryCode.value, phone.value), password))
//                    .onSuccess {
//                        analitycs.handleAnalyticMessage(AnalyticsLogInEvent)
//                        navigateToScreen(SCREENS.MAIN)
//                    }.onFailure {
//                        showLoader.value = false
//                        networkError.value = it
//                        networkConnectionValidator.validation()
//                    }
//            }
//        }
    }

    private fun checkFields(): Boolean {
        muxValidator.requestAllValidator()
        muxValidator.validate()
        return !muxValidator.getState().isThereError
    }

    fun navToSettings() {
        navigateToScreen(ACCESS_TO_SETTINGS)
    }
}

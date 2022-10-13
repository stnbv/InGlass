package com.inglass.android.presentation.auth_screens.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inglass.android.R
import com.inglass.android.domain.usecase.auth.LogInUseCase
import com.inglass.android.domain.usecase.auth.LogInUseCase.Params
import com.inglass.android.utils.ErrorWrapper
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.ErrorCode
import com.inglass.android.utils.api.core.onFailure
import com.inglass.android.utils.api.core.onSuccess
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.errors.conditions.NetworkConditionMessagesLogin
import com.inglass.android.utils.errors.observers.DefaultObserverValidate
import com.inglass.android.utils.errors.validators.Validator
import com.inglass.android.utils.navigation.DIALOGS.ACCESS_TO_SETTINGS
import com.inglass.android.utils.navigation.SCREENS.DESKTOP
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginVM @Inject constructor(private val logInUseCase: LogInUseCase) : BaseViewModel() {

    val showLoader = MutableLiveData(false)
    val phone = MutableLiveData("")
    val loginButtonVisibility = MutableLiveData(true)

    val password = MutableLiveData("")
    val passErrorWrapper = MutableLiveData<ErrorWrapper>(ErrorWrapper.None)

    val currentError = MutableLiveData<ErrorWrapper>(ErrorWrapper.None)
    private val networkError = MutableLiveData<Answer.Failure?>(null)

    private val networkConnectionValidator = Validator(networkError).apply {
        addCondition(
            NetworkConditionMessagesLogin(
                mapOf(ErrorCode.InternalError to R.string.error_internal_connectivity_error),
                useServerMessage = true
            )
        )
        setObserver(DefaultObserverValidate(currentError))
    }

    fun onLoginClick() {
        viewModelScope.launch {
            val password = password.value ?: return@launch
            val phone = phone.value ?: return@launch

            showLoader.value = true
            logInUseCase(Params(phone, password))
                .onSuccess {
                    navigateToScreen(DESKTOP)
                }.onFailure {
                    showLoader.value = false
                    networkError.value = it
                    networkConnectionValidator.validation()
                }
        }
    }

    fun navToSettings() {
        navigateToScreen(ACCESS_TO_SETTINGS)
    }
}

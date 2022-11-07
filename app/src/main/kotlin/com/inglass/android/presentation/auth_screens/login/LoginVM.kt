package com.inglass.android.presentation.auth_screens.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inglass.android.R
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.auth.LogInUseCase
import com.inglass.android.domain.usecase.auth.LogInUseCase.Params
import com.inglass.android.utils.ErrorWrapper
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.ErrorCode
import com.inglass.android.utils.api.core.onFailure
import com.inglass.android.utils.api.core.onSuccess
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.errors.conditions.LoginAndPassCondition
import com.inglass.android.utils.errors.conditions.NetworkConditionMessagesLogin
import com.inglass.android.utils.errors.observers.DefaultObserverValidate
import com.inglass.android.utils.errors.validators.Validator
import com.inglass.android.utils.navigation.DIALOGS.ACCESS_TO_SETTINGS
import com.inglass.android.utils.navigation.SCREENS.DESKTOP
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginVM @Inject constructor(
    private val logInUseCase: LogInUseCase,
    private val preferencesRepository: IPreferencesRepository
) : BaseViewModel() {

    val showLoader = MutableLiveData(false)
    val login = MutableLiveData(preferencesRepository.userLogin)

    val loginButtonVisibility = MutableLiveData(true)

    val password = MutableLiveData(preferencesRepository.userPassword)

    val currentError = MutableLiveData<ErrorWrapper>(ErrorWrapper.None)
    private val networkError = MutableLiveData<Answer.Failure?>(null)

    private val loginValidator = Validator<String>(login).apply {
        addCondition(LoginAndPassCondition())
        setObserver(DefaultObserverValidate(currentError))
    }

    private val passValidator = Validator<String>(password).apply {
        addCondition(LoginAndPassCondition())
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

    fun onLoginClick() {
        loginValidator.validation()
        passValidator.validation()

        if (loginValidator.getState().isThereError || passValidator.getState().isThereError) return

        viewModelScope.launch {
            val password = password.value ?: return@launch
            val login = login.value ?: return@launch

            showLoader.value = true
            logInUseCase(Params(login, password))
                .onSuccess {
                    preferencesRepository.userLogin = login
                    preferencesRepository.userPassword = password
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

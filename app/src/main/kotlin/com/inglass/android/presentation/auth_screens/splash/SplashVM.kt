package com.inglass.android.presentation.auth_screens.splash

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inglass.android.domain.models.AuthState
import com.inglass.android.domain.usecase.auth.AuthStateUseCase
import com.inglass.android.utils.analitics.AnalyticMessage.AnalyticsLogInEvent
import com.inglass.android.utils.api.core.onFailure
import com.inglass.android.utils.api.core.onSuccess
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.navigation.SCREENS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val VALUE_LOADING_START = "0"
const val VALUE_LOADING_FINISH = "100"
const val LOADING_TIME = 2000L
private const val LOADING_CHANGE_INTERVAL = 50L
private const val NUMBER = 30 // Что это за число ?

@HiltViewModel
class SplashVM @Inject constructor(private val authStateUseCase: AuthStateUseCase) : BaseViewModel() {

    val valuePercentLoad = MutableLiveData(VALUE_LOADING_START)

    var timer = object : CountDownTimer(LOADING_TIME, LOADING_CHANGE_INTERVAL) {

        override fun onTick(millisUntilFinished: Long) {
            val loadingPercentage = (LOADING_TIME - millisUntilFinished) / NUMBER
            valuePercentLoad.postValue("$loadingPercentage")
        }

        override fun onFinish() {
            valuePercentLoad.postValue(VALUE_LOADING_FINISH)
        }
    }

    init {
        timer.start()
    }

    fun getAuthState() {
        viewModelScope.launch(Dispatchers.Main) {
            authStateUseCase(Unit)
                .onSuccess { state ->
                    when (state) {
                        is AuthState.LoggedIn -> {
                            analitycs.handleAnalyticMessage(AnalyticsLogInEvent)
                            navigateToScreen(SCREENS.DESKTOP)
                        }
                        else -> navigateToScreen(SCREENS.LOGIN)
                    }
                }
                .onFailure {
                    navigateToScreen(SCREENS.LOGIN)
                    setError(it)
                }
        }
    }
}

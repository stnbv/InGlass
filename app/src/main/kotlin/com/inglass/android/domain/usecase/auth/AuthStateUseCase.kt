package com.inglass.android.domain.usecase.auth

import com.inglass.android.domain.models.AuthState
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.UseCase
import com.inglass.android.presentation.auth_screens.splash.LOADING_TIME
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.ErrorCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber

class AuthStateUseCase(private val repository: IPreferencesRepository) : UseCase<Unit, AuthState>() {

    override suspend operator fun invoke(params: Unit): Answer<AuthState> {
        return withContext(Dispatchers.IO) {
            delay(LOADING_TIME)
            try {
                when {
                    repository.token.isNullOrEmpty() -> Answer.success(AuthState.LoggedOut)
                    else -> Answer.success(AuthState.LoggedIn)
                }
            } catch (e: Exception) {
                Timber.d(e)
                Answer.failure(e, ErrorCode.InternalError)
            }
        }
    }
}

package com.inglass.android.data.remote.interceptors

import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.auth.LogInUseCase
import com.inglass.android.domain.usecase.auth.LogInUseCase.Params
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val preferences: IPreferencesRepository,
    private val logInUseCase: LogInUseCase
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            if (logInUseCase(Params(preferences.userLogin, preferences.userPassword)).isSuccess) {
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${preferences.token}")
                    .build()
            } else {
                null
            }
        }
    }
}

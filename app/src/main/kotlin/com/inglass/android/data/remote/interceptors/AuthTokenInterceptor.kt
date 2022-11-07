package com.inglass.android.data.remote.interceptors

import com.inglass.android.BuildConfig
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.auth.LogInUseCase
import com.inglass.android.domain.usecase.auth.LogInUseCase.Params
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor(
    private val logInUseCase: LogInUseCase,
    private val preferences: IPreferencesRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val result = sendRequest(chain)

        return if (result.code != HTTP_UNAUTHORIZED) {
            result
        } else {
            runBlocking {
                if (logInUseCase(Params(preferences.userLogin, preferences.userPassword)).isSuccess) {
                    sendRequest(chain)
                } else {
                    result
                }
            }
        }
    }

    private fun sendRequest(chain: Interceptor.Chain): Response {
        val token = preferences.token
        val originalRequest = chain.request()

        val newUrl: String = originalRequest.url.toString().replace(BuildConfig.BASE_URL, preferences.baseUrl)

        val newRequest = originalRequest.newBuilder().apply {
            url(newUrl)
            if (!token.isNullOrEmpty()) {
                addHeader(
                    "Accept",
                    "application/json"
                ) //TODO Заменить токен на строке ниже(сейчас тестовый предоставленный заказчиком)
                addHeader("Authorization", "Bearer $token")
            }
        }.build()

        return chain.proceed(newRequest)
    }
}

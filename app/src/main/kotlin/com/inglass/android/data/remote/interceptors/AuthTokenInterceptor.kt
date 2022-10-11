package com.inglass.android.data.remote.interceptors

import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor(private val preferences: IPreferencesRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //TODO Обработка скисшего токена (Бэкенд пока сделал на нескисаемом токене)(Alex 21/11/21)

        val token = preferences.token
        val originalRequest = chain.request()

        val newRequest = if (token.isNullOrEmpty()) {
            originalRequest.newBuilder().build()
        } else {
            originalRequest.newBuilder()
                .addHeader("Accept", "application/json") // Нужно ли это ? Alex(28.11.21)
                .addHeader("Authorization", token)
                .build()
        }

        val response = chain.proceed(newRequest)
        val tokenFromHeader = response.headers["authorization"]
        if (!tokenFromHeader.isNullOrEmpty()) {
            preferences.token = tokenFromHeader
        }

        return response
    }
}

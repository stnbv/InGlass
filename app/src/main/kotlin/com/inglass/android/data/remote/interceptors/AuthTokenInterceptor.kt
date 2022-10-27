package com.inglass.android.data.remote.interceptors

import com.inglass.android.BuildConfig
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor(private val preferences: IPreferencesRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //TODO Обработка скисшего токена
        val token = preferences.token
        val originalRequest = chain.request()

        val newUrl: String = originalRequest.url.toString().replace(BuildConfig.BASE_URL, preferences.baseUrl)

        val newRequest = originalRequest.newBuilder().apply {
            url(newUrl)
            if (!token.isNullOrEmpty()) {
//                addHeader("Accept", "application/json")
//                addHeader("Authorization", token)

                addHeader("Accept", "application/json") //TODO Заменить токен на строке ниже(сейчас тестовый предоставленный заказчиком)
                addHeader("Authorization", "Bearer $token")
            }
        }.build()

        return chain.proceed(newRequest)
    }
}

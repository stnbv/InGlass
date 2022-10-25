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
                addHeader("Accept", "application/json")
                addHeader("Authorization", token)

//                addHeader("Accept", "application/json") //TODO Заменить токен на строке ниже(сейчас тестовый предоставленный заказчиком)
//                addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJJRCI6ImI0MjMyYjc1LWQxZDYtMTFlYy1hNWU1LTAyMTEzMjI0MGRkMyIsIm5hbWUiOiLQktCw0YHQuNC70Y_QvdGB0LrQuNC5INCQ0YDRgtGR0Lwg0JDQu9C10LrRgdCw0L3QtNGA0L7QstC40YcifQ.qQDlDjKhxTOSYuh1Db7Qzi-1k5AUPR392bRSLoz2iYg")
            }
        }.build()

        return chain.proceed(newRequest)
    }
}

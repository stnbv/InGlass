package com.inglass.android.data.remote.api

import com.inglass.android.data.remote.requests.auth.LogInRequest
import com.inglass.android.data.remote.responses.auth.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IAuthApi {

    @POST("token-auth")
    suspend fun logIn(@Body logInRequest: LogInRequest): Response<AuthResponse>

}

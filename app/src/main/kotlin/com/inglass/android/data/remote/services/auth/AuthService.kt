package com.inglass.android.data.remote.services.auth

import com.inglass.android.data.remote.api.IAuthApi
import com.inglass.android.data.remote.requests.auth.LogInRequest
import com.inglass.android.utils.api.BaseService

class AuthService(private val api: IAuthApi) : IAuthService, BaseService() {

    override suspend fun logIn(phoneNumber: String, password: String) = apiCall {
        api.logIn(LogInRequest(phoneNumber, password))
    }
}

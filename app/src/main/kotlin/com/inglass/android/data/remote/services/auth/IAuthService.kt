package com.inglass.android.data.remote.services.auth

import com.inglass.android.data.remote.responses.auth.AuthResponse
import com.inglass.android.utils.api.core.Answer

interface IAuthService {
    suspend fun logIn(phoneNumber: Long, password: String): Answer<AuthResponse>
}

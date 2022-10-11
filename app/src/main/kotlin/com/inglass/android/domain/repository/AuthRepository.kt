package com.inglass.android.domain.repository

import com.inglass.android.data.remote.responses.auth.AuthResponse
import com.inglass.android.data.remote.responses.auth.toDomain
import com.inglass.android.data.remote.services.auth.IAuthService
import com.inglass.android.domain.models.auth.AuthModel
import com.inglass.android.domain.repository.interfaces.IAuthRepository
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.map

class AuthRepository(private val service: IAuthService) : IAuthRepository {

    override suspend fun logIn(phoneNumber: Long, password: String): Answer<AuthModel> {
        return service.logIn(phoneNumber, password).map(AuthResponse::toDomain)
    }
}

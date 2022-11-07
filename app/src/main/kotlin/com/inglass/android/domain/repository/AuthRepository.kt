package com.inglass.android.domain.repository

import com.inglass.android.data.remote.responses.auth.AuthResponse
import com.inglass.android.data.remote.responses.auth.toModel
import com.inglass.android.data.remote.services.auth.IAuthService
import com.inglass.android.domain.models.AuthData
import com.inglass.android.domain.repository.interfaces.IAuthRepository
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.map
import kotlinx.coroutines.flow.MutableSharedFlow

class AuthRepository(private val service: IAuthService) : IAuthRepository {

    override val logOut: MutableSharedFlow<Unit> = MutableSharedFlow()

    override suspend fun logOut() {
        logOut.emit(Unit)
    }

    override suspend fun logIn(phoneNumber: String, password: String): Answer<AuthData> {
        return service.logIn(phoneNumber, password).map(AuthResponse::toModel)
    }
}

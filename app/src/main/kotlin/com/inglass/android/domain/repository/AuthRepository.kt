package com.inglass.android.domain.repository

import com.inglass.android.data.remote.responses.auth.AuthResponse
import com.inglass.android.data.remote.responses.auth.toModel
import com.inglass.android.data.remote.services.auth.IAuthService
import com.inglass.android.domain.models.AuthData
import com.inglass.android.domain.repository.interfaces.IAuthRepository
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.map
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class AuthRepository(private val service: IAuthService) : IAuthRepository {

    private val _logOut = Channel<Unit>()
    override val logOut: Flow<Unit>
        get() = _logOut.receiveAsFlow()

    override fun logOut() {
        _logOut.trySend(Unit)
    }

    override suspend fun logIn(phoneNumber: String, password: String): Answer<AuthData> {
        return service.logIn(phoneNumber, password).map(AuthResponse::toModel)
    }
}

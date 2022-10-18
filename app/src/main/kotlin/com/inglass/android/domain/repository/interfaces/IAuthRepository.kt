package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.AuthData
import com.inglass.android.utils.api.core.Answer

interface IAuthRepository {

    suspend fun logIn(phoneNumber: String, password: String): Answer<AuthData>

}

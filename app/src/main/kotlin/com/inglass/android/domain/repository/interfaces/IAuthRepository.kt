package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.AuthData
import com.inglass.android.utils.api.core.Answer
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {

    val logOut: Flow<Unit>

    fun logOut()

    suspend fun logIn(phoneNumber: String, password: String): Answer<AuthData>

}

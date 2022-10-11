package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.auth.AuthModel
import com.inglass.android.domain.models.auth.ConfirmPhoneModel
import com.inglass.android.domain.models.auth.ConfirmRecoveryModel
import com.inglass.android.utils.api.core.Answer

interface IAuthRepository {
    suspend fun logIn(phoneNumber: Long, password: String): Answer<AuthModel>
}

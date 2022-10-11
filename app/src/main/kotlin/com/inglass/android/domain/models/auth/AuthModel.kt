package com.inglass.android.domain.models.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthModel(
    val id: String?,
    val phoneNumber: Long,
    val registrationState: RegistrationState
) : Parcelable

enum class RegistrationState {
    NOT_CONFIRMED,
    CONFIRMED,
    REGISTRED;
}

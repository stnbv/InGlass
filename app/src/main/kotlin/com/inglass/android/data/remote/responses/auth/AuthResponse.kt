package com.inglass.android.data.remote.responses.auth

import com.google.gson.annotations.SerializedName
import com.inglass.android.domain.models.auth.RegistrationState
import com.inglass.android.domain.models.auth.RegistrationState.CONFIRMED
import com.inglass.android.domain.models.auth.RegistrationState.NOT_CONFIRMED
import com.inglass.android.domain.models.auth.RegistrationState.REGISTRED
import com.inglass.android.domain.models.auth.AuthModel

/**
 *  Состояния поля: registrationState
 *  not_confirmed - состояние после ввода телефона и смс
 *  confirmed - после подтверждения
 *  registred - после полной регистрации
 */

data class AuthResponse(
    @SerializedName("data") val data: AuthData
)

data class AuthData(
    @SerializedName("id") val id: String?,
    @SerializedName("type") val type: String, // user
    @SerializedName("attributes") val attributes: Attributes
)

data class Attributes(
    @SerializedName("phone_number") val phoneNumber: Long,
    @SerializedName("registration_state") val registrationState: String,
)

fun AuthResponse.toDomain() = AuthModel(
    id = data.id,
    phoneNumber = data.attributes.phoneNumber,
    registrationState = setState(data.attributes.registrationState),
)

private fun setState(state: String): RegistrationState {
    return when (state) {
        "not_confirmed" -> NOT_CONFIRMED
        "confirmed" -> CONFIRMED
        else -> REGISTRED
    }
}

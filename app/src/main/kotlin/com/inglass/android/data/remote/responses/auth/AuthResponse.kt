package com.inglass.android.data.remote.responses.auth

import com.google.gson.annotations.SerializedName
import com.inglass.android.domain.models.AuthData

data class AuthResponse(
    @SerializedName("token") val token: String
)

fun AuthResponse.toModel() = AuthData(
    token = token
)

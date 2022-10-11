package com.inglass.android.data.remote.requests.auth

import com.google.gson.annotations.SerializedName

data class LogInRequest(
    @SerializedName("phone_number") val phoneNumber: Long,
    @SerializedName("password") val password: String
)

package com.inglass.android.data.remote.requests.auth

import com.google.gson.annotations.SerializedName

data class LogInRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String
)

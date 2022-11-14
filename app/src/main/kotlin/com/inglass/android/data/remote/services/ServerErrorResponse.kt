package com.inglass.android.data.remote.services

import com.google.gson.annotations.SerializedName

data class ServerErrorResponse(
    @SerializedName("error_code") val errorCode: Int?,
    @SerializedName("error_msg") val message: String?,
)

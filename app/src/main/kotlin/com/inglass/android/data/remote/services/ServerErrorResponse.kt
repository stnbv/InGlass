package com.inglass.android.data.remote.services

import com.google.gson.annotations.SerializedName

//TODO Поменять риспонс

data class ServerErrorResponse(
    @SerializedName("errors") val error: Error?
) {

    data class Error(
        @SerializedName("title") val message: String,
        @SerializedName("status") val status: Int,
        @SerializedName("meta") val meta: Meta?,
        @SerializedName("detail") val detail: String?
    )

    data class Meta(
        @SerializedName("time_to_next_attempt") val timeToNextAttempt: Int
    )
}

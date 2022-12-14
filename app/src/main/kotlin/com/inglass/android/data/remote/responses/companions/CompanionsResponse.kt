package com.inglass.android.data.remote.responses.companions

import com.google.gson.annotations.SerializedName
import com.inglass.android.domain.models.CompanionsModel

data class CompanionsResponse(
    @SerializedName("id") val id: String,
    @SerializedName("participation_rate") val participationRate: Float
)

fun CompanionsResponse.toModel() = CompanionsModel(
    id = id,
    participationRate = participationRate
)

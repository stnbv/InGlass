package com.inglass.android.data.remote.requests.auth

import com.google.gson.annotations.SerializedName

data class ItemOperationRequest(
    @SerializedName("employee") val employeeId: String,
    @SerializedName("operation") val operationId: String,
    @SerializedName("datetime") val dateTime: Long,
    @SerializedName("participation_rate") val participationRate: String,
    @SerializedName("companions") val helpers: List<Helper>,
    @SerializedName("items_qty") val itemsQty: Int,
)

data class Helper(
    @SerializedName("id") val id: Int,
    @SerializedName("participation_rate") val participationRate: Float
)

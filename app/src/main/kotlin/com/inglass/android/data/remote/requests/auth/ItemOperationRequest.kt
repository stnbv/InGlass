package com.inglass.android.data.remote.requests.auth

import com.google.gson.annotations.SerializedName

data class ItemOperationRequest(
    @SerializedName("employee") val employeeId: Int,
    @SerializedName("operation") val operationId: Int,
    @SerializedName("datetime") val dateTime: Long,
    @SerializedName("participation_rate") val participationRate: Float,
    @SerializedName("companions") val helpers: List<Helper>,
    @SerializedName("items_qty") val itemsQty: Int,
)

data class Helper(
    @SerializedName("id") val id: Int,
    @SerializedName("participation_rate") val participationRate: Float
)

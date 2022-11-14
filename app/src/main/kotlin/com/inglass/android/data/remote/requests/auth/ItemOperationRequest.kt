package com.inglass.android.data.remote.requests.auth

import com.google.gson.annotations.SerializedName
import com.inglass.android.domain.models.ScannedItemModel
import com.inglass.android.utils.helpers.DateFormatHelper.CreateProcedureTimeStamp

data class ItemOperationRequest(
    @SerializedName("employee") val employeeId: String,
    @SerializedName("operation") val operationId: String,
    @SerializedName("datetime") val dateTime: String,
    @SerializedName("participation_rate") val participationRate: Float,
    @SerializedName("companions") val helpers: List<Helper>,
    @SerializedName("items_qty") val itemsQty: Int,
)

data class Helper(
    @SerializedName("id") val id: String,
    @SerializedName("participation_rate") val participationRate: Float
)

fun ScannedItemModel.toRequestModel() = ItemOperationRequest(
    employeeId = employeeId,
    operationId = operationId,
    dateTime = CreateProcedureTimeStamp.format(dateTime),
    participationRate = participationRate,
    helpers = helpers.map {
        Helper(id = it.id, participationRate = it.participationRate)
    },
    itemsQty = itemsQty
)

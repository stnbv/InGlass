package com.inglass.android.domain.models

import java.util.*

data class ScannedItemModel(
    val employeeId: String,
    val operationId: String,
    val dateTime: Date,
    val participationRate: Float,
    val helpers: List<Helper>,
    val itemsQty: Int,
)

data class Helper(
    val id: String,
    val participationRate: Float
)

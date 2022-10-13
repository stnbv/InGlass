package com.inglass.android.domain.models

data class ScannedItemModel(
    val employeeId: Int,
    val operationId: Int,
    val dateTime: Long,
    val participationRate: Float,
    val helpers: List<Helper>,
    val itemsQty: Int,
)

data class Helper(
    val id: Int,
    val participationRate: Float
)

package com.inglass.android.domain.models

import java.util.*

data class ScannedItemModel(
    val employeeId: String,
    val operationId: String,
    val dateTime: Date,
    val participationRate: Float,
    val helpers: List<Helper>,
    val itemsQty: Int = 1
)

data class Helper(
    val id: String,
    val participationRate: Float
)

data class FullScannedItemModel(
    val barcode: String,
    val loadingStatus: LoadingStatus,
    val employeeId: String,
    val operationId: String,
    val dateTime: Date,
    val participationRate: Float,
    val helpers: List<Helper>,
    val itemsQty: Int = 1
)

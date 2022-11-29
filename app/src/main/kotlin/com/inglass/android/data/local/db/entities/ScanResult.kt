package com.inglass.android.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.inglass.android.domain.models.LoadingStatus
import java.util.*

@Entity(tableName = "scan_results")
data class ScanResult(
    @PrimaryKey
    val barcode: String,
    val operationId: String,
    val dateAndTime: Date,
    val loadingStatus: LoadingStatus,
    val error: String?
)

data class ScanResultFullInfo(
    val barcode: String,
    val operationId: String,
    val dateAndTime: Date,
    val loadingStatus: LoadingStatus,
    var operationName: String,
    val error: String?
)

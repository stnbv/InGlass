package com.inglass.android.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "scan_results")
data class ScanResult(
    @PrimaryKey
    val barcode: String,
    val operationId: Int,
    val dateAndTime: Date,
    var hasUploaded: Boolean = false
)

@Entity(tableName = "user_helpers")
data class UserHelpers(
    val helperId: Int,
    val participationRate: Float
)

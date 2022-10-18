package com.inglass.android.data.local.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.*

@Entity(tableName = "scan_results")
data class ScanResult(
    @PrimaryKey
    val barcode: String,
    val operationId: Int,
    val dateAndTime: Date,
    val loadingStatus: LoadingStatus
)

data class ScanResultWithOperation(
    @Embedded val scanResult: ScanResult,
    @Relation(
        parentColumn = "operationId",
        entityColumn = "operationId"
    )
    val operation: Operation?
)

enum class LoadingStatus {
    NotLoaded,
    Queue,
    InProgress,
    Loaded
}

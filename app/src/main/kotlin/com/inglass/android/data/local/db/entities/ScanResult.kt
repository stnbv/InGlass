package com.inglass.android.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "scan_results")
data class ScanResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(index = true)
    val barcode: String,
    val operation: String,
    val dateAndTime: Date,
    var employee: Long,
//    val helpers: List<String>?,
    @ColumnInfo(name = "has_uploaded")
    var hasUploaded: Boolean? = false
)

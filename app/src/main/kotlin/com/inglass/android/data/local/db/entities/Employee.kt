package com.inglass.android.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee (
    @PrimaryKey
    val id: Long,
    var login: String,
    val FirstName: String?,
    val LastName: String?
)
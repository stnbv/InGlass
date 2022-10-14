package com.inglass.android.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey
    val id: Long,
    var name: String,
)

@Entity(tableName = "options")
data class Options(
    @PrimaryKey
    val id: Long,
    var name: String,
)
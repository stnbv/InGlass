package com.inglass.android.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companions")
data class Companions(
    @PrimaryKey
    val helperId: String,
    val participationRate: Float
)

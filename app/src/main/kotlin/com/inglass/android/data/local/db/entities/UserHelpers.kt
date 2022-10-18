package com.inglass.android.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_helpers")
data class UserHelpers(
    @PrimaryKey
    val helperId: Int,
    val participationRate: Float
)

package com.inglass.android.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "user_helpers")
data class UserHelpers(
    @PrimaryKey
    val helperId: String,
    val participationRate: BigDecimal
)

data class HelperFullInfo(
    val id: String,
    val name: String,
    val participationRate: BigDecimal
)

package com.inglass.android.data.local.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "user_helpers",
    foreignKeys = [ForeignKey(
        entity = Employee::class,
        childColumns = ["helperId"],
        parentColumns = ["id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class UserHelpers(
    @PrimaryKey
    val helperId: Int,
    val participationRate: Float
)

data class UserHelpersWithName(
    @Embedded val userHelpers: UserHelpers,
    @Relation(
        parentColumn = "helperId",
        entityColumn = "id"
    )
    val employee: Employee
)

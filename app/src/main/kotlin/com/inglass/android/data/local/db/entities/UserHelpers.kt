package com.inglass.android.data.local.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.math.BigDecimal

//@Entity(
//    tableName = "user_helpers",
//    foreignKeys = [ForeignKey(
//        entity = Employee::class,
//        childColumns = ["helperId"],
//        parentColumns = ["id"],
//        onDelete = ForeignKey.CASCADE,
//        onUpdate = ForeignKey.CASCADE
//    )]
//)

data class UserHelpersWithName(
    @Embedded val userHelpers: UserHelpers,
    @Relation(
        parentColumn = "helperId",
        entityColumn = "id"
    )
    val employee: Employee
)

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

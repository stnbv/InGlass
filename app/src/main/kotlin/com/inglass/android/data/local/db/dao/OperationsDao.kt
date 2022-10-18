package com.inglass.android.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inglass.android.data.local.db.entities.Operation

@Dao
interface OperationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperations(operations: List<Operation>)

    @Query("delete from operations")
    suspend fun deleteAllOperations()
}
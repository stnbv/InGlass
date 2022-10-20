package com.inglass.android.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inglass.android.data.local.db.entities.UserHelpers
import com.inglass.android.data.local.db.entities.UserHelpersWithName

@Dao
interface UserHelpersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHelper(helper: UserHelpers)

    @Query("SELECT * FROM user_helpers")
    fun getAllHelpers(): PagingSource<Int, UserHelpersWithName>

    @Query("delete from user_helpers where helperId = :helperId ")
    suspend fun deleteHelper(helperId: Int)

    @Query("delete from user_helpers")
    suspend fun deleteAllHelpers()
}
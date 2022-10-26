package com.inglass.android.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inglass.android.data.local.db.entities.HelperFullInfo
import com.inglass.android.data.local.db.entities.UserHelpers
import kotlinx.coroutines.flow.Flow

@Dao
interface UserHelpersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHelpers(helpers: List<UserHelpers>)

    @Query("SELECT * FROM user_helpers")
    fun getHelpers(): List<UserHelpers>

    @Query(
        "SELECT user_helpers.participationRate AS participationRate, employees.name AS name, user_helpers.helperId AS id " +
                "FROM user_helpers, employees " +
                "WHERE user_helpers.helperId = employees.id"
    )
    fun getHelperFullInfoFlow(): Flow<List<HelperFullInfo>>

    @Query(
        "SELECT user_helpers.participationRate AS participationRate, employees.name AS name, user_helpers.helperId AS id " +
                "FROM user_helpers, employees " +
                "WHERE user_helpers.helperId = employees.id"
    )
    fun getHelperFullInfo(): List<HelperFullInfo>

    @Query("delete from user_helpers where helperId = :helperId ")
    suspend fun deleteHelper(helperId: String)

    @Query("delete from user_helpers")
    suspend fun deleteAllHelpers()
}

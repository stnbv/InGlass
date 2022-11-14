package com.inglass.android.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inglass.android.data.local.db.entities.Companions
import com.inglass.android.domain.models.CompanionsFullInfoModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CompanionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanions(helpers: List<Companions>)

    @Query(
        "SELECT companions.participationRate AS participationRate, employees.name AS name, companions.helperId AS id " +
                "FROM companions, employees " +
                "WHERE companions.helperId = employees.id"
    )
    fun getCompanionsFullInfoFlow(): Flow<List<CompanionsFullInfoModel>>

    @Query(
        "SELECT companions.participationRate AS participationRate, employees.name AS name, companions.helperId AS id " +
                "FROM companions, employees " +
                "WHERE companions.helperId = employees.id"
    )
    fun getCompanionsFullInfo(): List<CompanionsFullInfoModel>

    @Query("delete from companions")
    suspend fun deleteAllCompanions()
}

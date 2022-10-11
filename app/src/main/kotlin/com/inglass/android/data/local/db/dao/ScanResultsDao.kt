package com.inglass.android.data.local.db.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.inglass.android.data.local.db.entities.ScanResult

@Dao
interface ScanResultsDao {
    @Insert(onConflict = REPLACE)
    fun insertScanResult(scanResult: ScanResult)

    @Query("select * from scan_results")
    fun getScannedItems(): List<ScanResult>

    @Query("delete from scan_results")
    fun deleteAllItems()
}
package com.inglass.android.data.local.db.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.google.mlkit.vision.barcode.common.Barcode
import com.inglass.android.data.local.db.entities.ScanResult

@Dao
interface ScanResultsDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertScanResult(scanResult: ScanResult)

    @Query("select * from scan_results")
    suspend fun getScannedItems(): List<ScanResult>

    @Query("delete from scan_results")
    suspend fun deleteAllItems()

    @Query("select * from scan_results where barcode = :barcode")
    suspend fun getItemById(barcode: String): ScanResult?
}

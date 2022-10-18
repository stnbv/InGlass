package com.inglass.android.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.inglass.android.data.local.db.entities.LoadingStatus
import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.data.local.db.entities.ScanResultWithOperation

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

    @Query("SELECT * FROM scan_results")
    fun getScanResultWithOperation(): PagingSource<Int, ScanResultWithOperation>

    @Query("UPDATE scan_results SET loadingStatus = :loadingStatus WHERE barcode = :barcode")
    fun updateScanResult(barcode: String, loadingStatus: LoadingStatus)
}

package com.inglass.android.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.data.local.db.entities.ScanResultFullInfo
import com.inglass.android.domain.models.LoadingStatus

@Dao
interface ScanResultsDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertScanResult(scanResult: ScanResult)

    @Query("delete from scan_results")
    suspend fun deleteAllItems()

    @Query("select * from scan_results where barcode = :barcode")
    suspend fun getItemById(barcode: String): ScanResult?

    @Query("UPDATE scan_results SET loadingStatus = :loadingStatus, error = :error WHERE barcode = :barcode")
    suspend fun updateScanResult(barcode: String, loadingStatus: LoadingStatus, error: String?)

    @Query("select * from scan_results where loadingStatus in (:status)")
    suspend fun getItemsByStatus(status: List<LoadingStatus>): List<ScanResult>

    @Query(
        "SELECT scan_results.barcode AS barcode, scan_results.dateAndTime AS dateAndTime," +
                "scan_results.loadingStatus AS loadingStatus, scan_results.operationId AS operationId, " +
                "operations.name AS operationName, scan_results.error AS error " +
                "FROM scan_results, operations " +
                "WHERE scan_results.operationId = operations.operationId ORDER BY dateAndTime DESC "
    )
    fun getScanResultWithOperation(): PagingSource<Int, ScanResultFullInfo>
}

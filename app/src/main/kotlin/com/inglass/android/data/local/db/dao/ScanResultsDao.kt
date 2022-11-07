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

    @Query(
        "SELECT scan_results.barcode AS barcode, scan_results.dateAndTime AS dateAndTime," +
                "scan_results.loadingStatus AS loadingStatus, scan_results.operationId AS operationId, " +
                "operations.name AS operationName " +
                "FROM scan_results, operations " +
                "WHERE scan_results.operationId = operations.operationId ORDER BY dateAndTime DESC "
    )
    fun getScanResultWithOperation(): PagingSource<Int, ScanResultFullInfo>

    @Query("UPDATE scan_results SET loadingStatus = :loadingStatus WHERE barcode = :barcode")
    fun updateScanResult(barcode: String, loadingStatus: LoadingStatus)
}

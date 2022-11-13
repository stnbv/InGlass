package com.inglass.android.domain.repository.interfaces

import androidx.paging.PagingSource
import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.data.local.db.entities.ScanResultFullInfo
import com.inglass.android.domain.models.FullScannedItemModel
import com.inglass.android.domain.models.LoadingStatus
import kotlinx.coroutines.channels.Channel


interface IScanResultsRepository {

    val result: Channel<FullScannedItemModel>

    suspend fun emitScanResult(scanResult: FullScannedItemModel)

    suspend fun saveScanResult(scanResult: ScanResult)

    suspend fun deleteAllItems()

    suspend fun getItemById(barcode: String): ScanResult?

    suspend fun updateScanResult(barcode: String, loadingStatus: LoadingStatus)

    fun getScanResultWithOperation(): PagingSource<Int, ScanResultFullInfo>

}
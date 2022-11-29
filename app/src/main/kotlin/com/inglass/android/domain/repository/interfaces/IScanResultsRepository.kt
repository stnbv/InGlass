package com.inglass.android.domain.repository.interfaces

import androidx.paging.PagingSource
import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.data.local.db.entities.ScanResultFullInfo
import com.inglass.android.domain.models.FullScannedItemModel
import com.inglass.android.domain.models.LoadingStatus
import com.inglass.android.domain.models.SecondaryScanInfo
import kotlinx.coroutines.channels.Channel

interface IScanResultsRepository {

    val result: Channel<FullScannedItemModel>

    suspend fun emitScanResult(scanResult: ScanResult)

    suspend fun saveScanResult(scanResult: ScanResult)

    suspend fun deleteAllItems()

    suspend fun getItemById(barcode: String): ScanResult?

    suspend fun updateScanResult(barcode: String, loadingStatus: LoadingStatus, error: String?)

    suspend fun getItemsByStatus(status: List<LoadingStatus>): List<ScanResult>

    fun getScanResultWithOperation(): PagingSource<Int, ScanResultFullInfo>

    fun recreateResultsChannel()

    fun initSecondaryScanInfo(secondaryInfo: SecondaryScanInfo)

}
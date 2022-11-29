package com.inglass.android.domain.repository

import androidx.paging.PagingSource
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.data.local.db.entities.ScanResultFullInfo
import com.inglass.android.domain.models.FullScannedItemModel
import com.inglass.android.domain.models.LoadingStatus
import com.inglass.android.domain.models.LoadingStatus.Queue
import com.inglass.android.domain.models.SecondaryScanInfo
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED

class ScanResultsRepository(private val scanResultsDao: ScanResultsDao) : IScanResultsRepository {

    private var info: SecondaryScanInfo? = null
    override var result = Channel<FullScannedItemModel>(UNLIMITED)

    override suspend fun emitScanResult(scanResult: ScanResult) {
        result.send(
            FullScannedItemModel(
                barcode = scanResult.barcode,
                loadingStatus = scanResult.loadingStatus,
                employeeId = info?.employeeId ?: return,
                operationId = scanResult.operationId,
                dateTime = scanResult.dateAndTime,
                participationRate = info?.participationRate ?: return,
                helpers = info?.helpers ?: return
            )
        )
    }

    override suspend fun saveScanResult(scanResult: ScanResult) {
        scanResultsDao.insertScanResult(scanResult)
    }

    override suspend fun deleteAllItems() {
        scanResultsDao.deleteAllItems()
    }

    override suspend fun getItemById(barcode: String): ScanResult? {
        return scanResultsDao.getItemById(barcode)
    }

    override fun getScanResultWithOperation(): PagingSource<Int, ScanResultFullInfo> {
        return scanResultsDao.getScanResultWithOperation()
    }

    override suspend fun updateScanResult(barcode: String, loadingStatus: LoadingStatus, error: String?) {
        scanResultsDao.updateScanResult(barcode, loadingStatus, error)
    }

    override suspend fun getItemsByStatus(status: List<LoadingStatus>): List<ScanResult> {
        return scanResultsDao.getItemsByStatus(status)
    }

    override fun recreateResultsChannel() {
        result.cancel()
        result = Channel<FullScannedItemModel>(UNLIMITED)
    }

    override fun initSecondaryScanInfo(secondaryInfo: SecondaryScanInfo) {
        info = secondaryInfo
    }
}

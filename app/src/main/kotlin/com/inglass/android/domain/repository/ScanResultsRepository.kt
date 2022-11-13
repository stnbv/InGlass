package com.inglass.android.domain.repository

import androidx.paging.PagingSource
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.data.local.db.entities.ScanResultFullInfo
import com.inglass.android.domain.models.FullScannedItemModel
import com.inglass.android.domain.models.LoadingStatus
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED

class ScanResultsRepository(private val scanResultsDao: ScanResultsDao) : IScanResultsRepository {

    override val result = Channel<FullScannedItemModel>(UNLIMITED)

    override suspend fun emitScanResult(scanResult: FullScannedItemModel) {
        result.send(scanResult)
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

    override suspend fun updateScanResult(barcode: String, loadingStatus: LoadingStatus) {
        scanResultsDao.updateScanResult(barcode, loadingStatus)
    }
}

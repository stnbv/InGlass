package com.inglass.android.domain.repository

import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
import kotlinx.coroutines.flow.MutableSharedFlow

class ScanResultsRepository : IScanResultsRepository {

    override val result = MutableSharedFlow<ScanResult>()

    override suspend fun emitScanResult(scanResult: ScanResult) {
        result.emit(scanResult)
    }
}
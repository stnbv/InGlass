package com.inglass.android.domain.repository

import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED

class ScanResultsRepository : IScanResultsRepository {

    override val result = Channel<ScanResult>(UNLIMITED)

    override suspend fun emitScanResult(scanResult: ScanResult) {
        result.send(scanResult)
    }
}

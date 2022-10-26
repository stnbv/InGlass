package com.inglass.android.domain.repository

import com.inglass.android.domain.models.FullScannedItemModel
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED

class ScanResultsRepository : IScanResultsRepository {

    override val result = Channel<FullScannedItemModel>(UNLIMITED)

    override suspend fun emitScanResult(scanResult: FullScannedItemModel) {
        result.send(scanResult)
    }
}

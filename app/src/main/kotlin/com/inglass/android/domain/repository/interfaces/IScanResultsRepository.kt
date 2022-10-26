package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.FullScannedItemModel
import kotlinx.coroutines.channels.Channel


interface IScanResultsRepository {

    val result: Channel<FullScannedItemModel>

    suspend fun emitScanResult(scanResult: FullScannedItemModel)
}
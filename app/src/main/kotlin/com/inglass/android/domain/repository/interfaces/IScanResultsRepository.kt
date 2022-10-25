package com.inglass.android.domain.repository.interfaces

import com.inglass.android.data.local.db.entities.ScanResult
import kotlinx.coroutines.channels.Channel


interface IScanResultsRepository {

    val result: Channel<ScanResult>

    suspend fun emitScanResult(scanResult: ScanResult)
}
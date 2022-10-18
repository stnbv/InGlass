package com.inglass.android.domain.repository.interfaces

import com.inglass.android.data.local.db.entities.ScanResult
import kotlinx.coroutines.flow.Flow

interface IScanResultsRepository {

    val result: Flow<ScanResult>

    suspend fun emitScanResult(scanResult: ScanResult)
}
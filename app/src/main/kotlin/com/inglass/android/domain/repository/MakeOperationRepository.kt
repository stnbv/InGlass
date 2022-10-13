package com.inglass.android.domain.repository

import com.inglass.android.data.remote.services.make_operation.IMakeOperationService
import com.inglass.android.domain.models.ScannedItemModel
import com.inglass.android.domain.repository.interfaces.IMakeOperationRepository

class MakeOperationRepository(
    private val service: IMakeOperationService
) : IMakeOperationRepository {

    override suspend fun scanItem(itemId: String, scannedItem: ScannedItemModel) =
        service.makeOperation(itemId, scannedItem)
}

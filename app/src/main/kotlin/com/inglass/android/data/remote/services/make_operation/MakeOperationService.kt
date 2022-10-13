package com.inglass.android.data.remote.services.make_operation

import com.inglass.android.data.remote.api.IMakeOperationApi
import com.inglass.android.data.remote.requests.auth.Helper
import com.inglass.android.data.remote.requests.auth.ItemOperationRequest
import com.inglass.android.domain.models.ScannedItemModel
import com.inglass.android.utils.api.BaseService

class MakeOperationService(private val api: IMakeOperationApi) : IMakeOperationService, BaseService() {

    override suspend fun makeOperation(itemId: String, scannedItemModel: ScannedItemModel) = apiCall {

        val requestModel = ItemOperationRequest(
            employeeId = scannedItemModel.employeeId,
            operationId = scannedItemModel.operationId,
            dateTime = scannedItemModel.dateTime,
            participationRate = scannedItemModel.participationRate,
            helpers = scannedItemModel.helpers.map {
                Helper(id = it.id, participationRate = it.participationRate)
            },
            itemsQty = scannedItemModel.itemsQty
        )
        api.makeOperation(itemId, requestModel)
    }

}

package com.inglass.android.data.remote.services.make_operation

import com.inglass.android.data.remote.api.IMakeOperationApi
import com.inglass.android.data.remote.requests.auth.toRequestModel
import com.inglass.android.domain.models.ScannedItemModel
import com.inglass.android.utils.api.BaseService

class MakeOperationService(private val api: IMakeOperationApi) : IMakeOperationService, BaseService() {

    override suspend fun makeOperation(itemId: String, scannedItemModel: ScannedItemModel) = apiCall {
        val requestModel = scannedItemModel.toRequestModel()
        api.makeOperation(itemId, requestModel)
    }
}

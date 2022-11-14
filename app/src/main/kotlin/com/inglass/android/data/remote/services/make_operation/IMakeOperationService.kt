package com.inglass.android.data.remote.services.make_operation

import com.inglass.android.domain.models.ScannedItemModel
import com.inglass.android.utils.api.core.Answer

interface IMakeOperationService {

    suspend fun makeOperation(itemId: String, scannedItemModel: ScannedItemModel): Answer<Unit>

}

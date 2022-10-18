package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.ScannedItemModel
import com.inglass.android.utils.api.core.Answer

interface IMakeOperationRepository {

    suspend fun scanItem(itemId: String, scannedItem: ScannedItemModel): Answer<Unit>

}

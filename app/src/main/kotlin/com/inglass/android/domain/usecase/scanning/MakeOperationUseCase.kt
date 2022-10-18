package com.inglass.android.domain.usecase.scanning

import com.inglass.android.domain.models.ScannedItemModel
import com.inglass.android.domain.repository.interfaces.IMakeOperationRepository
import com.inglass.android.domain.usecase.UseCase
import com.inglass.android.domain.usecase.scanning.MakeOperationUseCase.Params
import com.inglass.android.utils.api.core.Answer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MakeOperationUseCase(private val repository: IMakeOperationRepository) : UseCase<Params, Unit>() {

    override suspend fun invoke(params: Params): Answer<Unit> {
        return withContext(Dispatchers.IO) {
            repository.scanItem(params.itemId, params.scannedItem)
        }
    }

    class Params(
        val itemId: String,
        val scannedItem: ScannedItemModel
    )
}

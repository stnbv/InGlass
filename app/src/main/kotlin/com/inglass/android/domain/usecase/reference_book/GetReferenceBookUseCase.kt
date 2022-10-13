package com.inglass.android.domain.usecase.reference_book

import com.inglass.android.domain.repository.interfaces.IReferenceBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetReferenceBookUseCase(private val repository: IReferenceBookRepository) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        repository.getReferenceBook()
    }
}

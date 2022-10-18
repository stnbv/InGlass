package com.inglass.android.domain.repository

import com.inglass.android.data.remote.responses.reference_book.ReferenceBookResponse
import com.inglass.android.data.remote.responses.reference_book.toModel
import com.inglass.android.data.remote.services.reference_book.IReferenceBookService
import com.inglass.android.domain.models.ReferenceBookModel
import com.inglass.android.domain.repository.interfaces.IReferenceBookRepository
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.map

class ReferenceBookRepository(private val service: IReferenceBookService) : IReferenceBookRepository {

    override suspend fun getReferenceBook(): Answer<ReferenceBookModel> {
        return service.getReferenceBook().map(ReferenceBookResponse::toModel)
    }
}

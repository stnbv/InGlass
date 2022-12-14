package com.inglass.android.data.remote.services.reference_book

import com.inglass.android.data.remote.api.IReferenceBooksApi
import com.inglass.android.data.remote.responses.reference_book.ReferenceBookResponse
import com.inglass.android.utils.api.BaseService
import com.inglass.android.utils.api.core.Answer

class ReferenceBookService(private val api: IReferenceBooksApi) : IReferenceBookService, BaseService() {

    override suspend fun getReferenceBook(): Answer<ReferenceBookResponse> = apiCall {
        api.getReferenceBook()
    }
}

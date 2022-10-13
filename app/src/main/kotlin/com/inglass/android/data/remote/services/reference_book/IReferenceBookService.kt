package com.inglass.android.data.remote.services.reference_book

import com.inglass.android.data.remote.responses.reference_book.ReferenceBookResponse
import com.inglass.android.utils.api.core.Answer

interface IReferenceBookService {
    suspend fun getReferenceBook(): Answer<ReferenceBookResponse>
}

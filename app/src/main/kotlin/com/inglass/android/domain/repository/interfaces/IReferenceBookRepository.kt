package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.ReferenceBookModel
import com.inglass.android.utils.api.core.Answer

interface IReferenceBookRepository {

    suspend fun getReferenceBook(): Answer<ReferenceBookModel>

}

package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.CompanionsModel
import com.inglass.android.utils.api.core.Answer

interface ICompanionsRepository {

    suspend fun getCompanions(): Answer<List<CompanionsModel>>

}
package com.inglass.android.domain.repository

import com.inglass.android.data.remote.responses.companions.CompanionsResponse
import com.inglass.android.data.remote.responses.companions.toModel
import com.inglass.android.data.remote.services.companions.ICompanionsService
import com.inglass.android.domain.models.CompanionsModel
import com.inglass.android.domain.repository.interfaces.ICompanionsRepository
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.map

class CompanionsRepository(private val service: ICompanionsService) : ICompanionsRepository {

    override suspend fun getCompanions(): Answer<List<CompanionsModel>> {
        return service.getCompanions().map {
            it.map(CompanionsResponse::toModel)
        }
    }
}

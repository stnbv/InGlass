package com.inglass.android.domain.repository

import com.inglass.android.data.local.db.dao.CompanionsDao
import com.inglass.android.data.local.db.entities.Companions
import com.inglass.android.data.remote.responses.companions.CompanionsResponse
import com.inglass.android.data.remote.responses.companions.toModel
import com.inglass.android.data.remote.services.companions.ICompanionsService
import com.inglass.android.domain.models.CompanionsFullInfoModel
import com.inglass.android.domain.models.CompanionsModel
import com.inglass.android.domain.repository.interfaces.ICompanionsRepository
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.map

class CompanionsRepository(
    private val service: ICompanionsService,
    private val companionsDao: CompanionsDao
) : ICompanionsRepository {

    override suspend fun getCompanions(): Answer<List<CompanionsModel>> {
        return service.getCompanions().map {
            it.map(CompanionsResponse::toModel)
        }
    }

    override suspend fun saveCompanions(helpers: List<Companions>) {
        companionsDao.insertCompanions(helpers)
    }

    override suspend fun getCompanionsFullInfo(): List<CompanionsFullInfoModel> {
        return companionsDao.getCompanionsFullInfo()
    }

    override suspend fun deleteAllCompanions() {
        companionsDao.deleteAllCompanions()
    }
}

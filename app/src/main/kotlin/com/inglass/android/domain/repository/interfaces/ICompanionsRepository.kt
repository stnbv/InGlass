package com.inglass.android.domain.repository.interfaces

import com.inglass.android.data.local.db.entities.Companions
import com.inglass.android.domain.models.CompanionsFullInfoModel
import com.inglass.android.domain.models.CompanionsModel
import com.inglass.android.utils.api.core.Answer

interface ICompanionsRepository {

    suspend fun getCompanions(): Answer<List<CompanionsModel>>

    suspend fun saveCompanions(helpers: List<Companions>)

    suspend fun getCompanionsFullInfo(): List<CompanionsFullInfoModel>

    suspend fun deleteAllCompanions()

}
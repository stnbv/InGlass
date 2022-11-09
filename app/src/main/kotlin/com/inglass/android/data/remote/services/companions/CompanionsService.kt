package com.inglass.android.data.remote.services.companions

import com.inglass.android.data.remote.api.ICompanionsApi
import com.inglass.android.data.remote.responses.companions.CompanionsResponse
import com.inglass.android.utils.api.BaseService
import com.inglass.android.utils.api.core.Answer


class CompanionsService(private val api: ICompanionsApi) : ICompanionsService, BaseService() {

    override suspend fun getCompanions(): Answer<List<CompanionsResponse>> = apiCall {
        api.getCompanions()
    }
}
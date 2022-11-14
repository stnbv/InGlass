package com.inglass.android.data.remote.services.companions

import com.inglass.android.data.remote.responses.companions.CompanionsResponse
import com.inglass.android.utils.api.core.Answer

interface ICompanionsService {

    suspend fun getCompanions(): Answer<List<CompanionsResponse>>

}

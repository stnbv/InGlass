package com.inglass.android.data.remote.services.personal_information

import com.inglass.android.data.remote.api.IPersonalInformationApi
import com.inglass.android.data.remote.responses.PersonalInformationResponse
import com.inglass.android.utils.api.BaseService
import com.inglass.android.utils.api.core.Answer

class PersonalInformationService(
    private val api: IPersonalInformationApi
) : IPersonalInformationService, BaseService() {

    override suspend fun getPersonalInformation(): Answer<PersonalInformationResponse> = apiCall {
        api.getPersonalInformation()
    }
}

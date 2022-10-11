package com.inglass.android.data.remote.services.personal_information

import com.inglass.android.data.remote.responses.PersonalInformationResponse
import com.inglass.android.utils.api.core.Answer

interface IPersonalInformationService {
    suspend fun getPersonalInformation(): Answer<PersonalInformationResponse>
}

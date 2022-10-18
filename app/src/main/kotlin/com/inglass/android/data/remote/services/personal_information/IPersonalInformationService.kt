package com.inglass.android.data.remote.services.personal_information

import com.inglass.android.data.remote.responses.personal_information.PersonalInformationResponse
import com.inglass.android.utils.api.core.Answer

interface IPersonalInformationService {

    suspend fun getPersonalInformation(): Answer<PersonalInformationResponse>

}

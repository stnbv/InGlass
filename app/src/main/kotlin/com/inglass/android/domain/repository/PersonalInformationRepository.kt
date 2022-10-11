package com.inglass.android.domain.repository

import com.inglass.android.data.remote.services.personal_information.IPersonalInformationService
import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.domain.repository.interfaces.IPersonalInformationRepository
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.utils.api.core.Answer

class PersonalInformationRepository(
    private val service: IPersonalInformationService,
    private val preferencesRepository: IPreferencesRepository
) : IPersonalInformationRepository {

    override suspend fun getPersonalInformation(): Answer<PersonalInformationModel?> {
        return Answer(preferencesRepository.user)
    }
}

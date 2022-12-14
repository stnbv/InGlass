package com.inglass.android.domain.repository

import com.inglass.android.data.remote.responses.personal_information.PersonalInformationResponse
import com.inglass.android.data.remote.responses.personal_information.toModel
import com.inglass.android.data.remote.services.personal_information.IPersonalInformationService
import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.domain.repository.interfaces.IPersonalInformationRepository
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.utils.api.core.map
import com.inglass.android.utils.api.core.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow

class PersonalInformationRepository(
    private val service: IPersonalInformationService,
    private val preferencesRepository: IPreferencesRepository
) : IPersonalInformationRepository {

    override val result: MutableStateFlow<PersonalInformationModel?> = MutableStateFlow(preferencesRepository.user)

    override suspend fun getPersonalInformation() {
        service.getPersonalInformation()
            .map(PersonalInformationResponse::toModel)
            .onSuccess {
                preferencesRepository.user = it
                result.emit(it)
            }
    }
}

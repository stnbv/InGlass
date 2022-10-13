package com.inglass.android.domain.repository

import com.inglass.android.data.remote.responses.personal_information.PersonalInformationResponse
import com.inglass.android.data.remote.responses.personal_information.toModel
import com.inglass.android.data.remote.services.personal_information.IPersonalInformationService
import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.domain.repository.interfaces.IPersonalInformationRepository
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.utils.api.core.map
import com.inglass.android.utils.api.core.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PersonalInformationRepository(
    private val service: IPersonalInformationService,
    private val preferencesRepository: IPreferencesRepository
) : IPersonalInformationRepository {

    override suspend fun getPersonalInformation(): Flow<PersonalInformationModel> {
        return flow {
            preferencesRepository.user?.let { emit(it) }

            service.getPersonalInformation()
                .map(PersonalInformationResponse::toModel)
                .onSuccess {
                    preferencesRepository.user = it
                    emit(it)
                }
        }
    }
}

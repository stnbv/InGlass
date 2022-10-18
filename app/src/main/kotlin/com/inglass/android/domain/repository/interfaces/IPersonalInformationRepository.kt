package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.PersonalInformationModel
import kotlinx.coroutines.flow.Flow

interface IPersonalInformationRepository {

    suspend fun getPersonalInformation(): Flow<PersonalInformationModel>

}

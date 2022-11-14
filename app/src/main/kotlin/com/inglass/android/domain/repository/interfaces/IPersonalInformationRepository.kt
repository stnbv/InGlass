package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.PersonalInformationModel
import kotlinx.coroutines.flow.Flow

interface IPersonalInformationRepository {

    val result: Flow<PersonalInformationModel?>

    suspend fun getPersonalInformation()

}

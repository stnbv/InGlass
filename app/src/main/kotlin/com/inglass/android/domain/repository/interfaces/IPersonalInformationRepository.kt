package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.utils.api.core.Answer
import kotlinx.coroutines.flow.Flow

interface IPersonalInformationRepository {
    suspend fun getPersonalInformation(): Answer<PersonalInformationModel?>

}

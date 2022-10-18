package com.inglass.android.domain.usecase.personal_information

import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.domain.repository.interfaces.IPersonalInformationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetPersonalInformationUseCase(private val repository: IPersonalInformationRepository) {

    suspend operator fun invoke(): Flow<PersonalInformationModel> {
        return withContext(Dispatchers.IO) {
            repository.getPersonalInformation()
        }
    }
}

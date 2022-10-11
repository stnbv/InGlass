package com.inglass.android.domain.usecase.personal_information

import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.domain.repository.interfaces.IPersonalInformationRepository
import com.inglass.android.domain.usecase.UseCase
import com.inglass.android.utils.api.core.Answer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetPersonalInformationUseCase(private val repository: IPersonalInformationRepository) :
    UseCase<Unit, PersonalInformationModel?>() {

    override suspend fun invoke(params: Unit): Answer<PersonalInformationModel?> {
        return withContext(Dispatchers.IO) {
            repository.getPersonalInformation()
        }
    }
}

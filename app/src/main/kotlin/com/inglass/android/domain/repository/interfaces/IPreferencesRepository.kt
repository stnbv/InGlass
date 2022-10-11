package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.Country
import com.inglass.android.domain.models.PersonalInformationModel

interface IPreferencesRepository {
    var token: String?
    var user: PersonalInformationModel?
    var isOnboardNotPassed: Boolean
    suspend fun clear()
}

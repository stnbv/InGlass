package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.PersonalInformationModel

interface IPreferencesRepository {
    var baseUrl: String
    var token: String?
    var user: PersonalInformationModel?
    var isOnboardNotPassed: Boolean
    var lastReceivedData: Long
    suspend fun clear()
}

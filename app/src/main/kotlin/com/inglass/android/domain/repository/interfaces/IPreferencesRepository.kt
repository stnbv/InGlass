package com.inglass.android.domain.repository.interfaces

import com.inglass.android.domain.models.PersonalInformationModel

interface IPreferencesRepository {
    var userLogin: String
    var userPassword: String
    var baseUrl: String
    var token: String?
    var user: PersonalInformationModel?
    var lastReceivedData: Long
    suspend fun clear()
}

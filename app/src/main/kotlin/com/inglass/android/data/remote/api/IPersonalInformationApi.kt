package com.inglass.android.data.remote.api

import com.inglass.android.data.remote.responses.personal_information.PersonalInformationResponse
import retrofit2.Response
import retrofit2.http.GET

interface IPersonalInformationApi {

    @GET("user")
    suspend fun getPersonalInformation(): Response<PersonalInformationResponse>

}

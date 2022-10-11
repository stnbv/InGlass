package com.inglass.android.data.remote.api

import com.inglass.android.data.remote.responses.PersonalInformationResponse
import retrofit2.Response
import retrofit2.http.GET

interface IPersonalInformationApi {

    @GET("profile")
    suspend fun getPersonalInformation(): Response<PersonalInformationResponse>
}

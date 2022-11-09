package com.inglass.android.data.remote.api

import com.inglass.android.data.remote.responses.companions.CompanionsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ICompanionsApi {

    @GET("companions")
    suspend fun getCompanions(): Response<List<CompanionsResponse>>

}
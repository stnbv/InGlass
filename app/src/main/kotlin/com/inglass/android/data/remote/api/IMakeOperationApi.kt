package com.inglass.android.data.remote.api

import com.inglass.android.data.remote.requests.auth.ItemOperationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface IMakeOperationApi {

    @POST("makeoperation/{itemId}")
    suspend fun makeOperation(
        @Path("itemId") itemId: String,
        @Body itemOperationRequest: ItemOperationRequest
    ): Response<Unit>

}

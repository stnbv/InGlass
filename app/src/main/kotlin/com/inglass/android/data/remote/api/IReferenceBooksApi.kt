package com.inglass.android.data.remote.api

import com.inglass.android.data.remote.responses.reference_book.ReferenceBookResponse
import retrofit2.Response
import retrofit2.http.GET

interface IReferenceBooksApi {

    @GET("refs")
    suspend fun getReferenceBook(): Response<ReferenceBookResponse>
}

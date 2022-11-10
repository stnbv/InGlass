package com.inglass.android.utils.api

import com.inglass.android.data.remote.services.ServerErrorResponse
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.ErrorCode
import com.inglass.android.utils.api.core.NoException
import com.inglass.android.utils.helpers.fromJson
import okhttp3.ResponseBody
import retrofit2.Response

abstract class BaseService {

    protected suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Answer<T> {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (e: Exception) {
            return Answer.failure(ex = e, code = ErrorCode.InternalError)
        }
        return if (!response.isSuccessful) {
            Answer.failure(parseError(response.code(), response.errorBody()))
        } else {
            println("response ${response.body()}")
            if (response.body() == null) {
                Answer.failure(ErrorCode.EmptyResponseError)
            } else {
                Answer.success(response.body()!!)
            }
        }
    }

    private fun parseError(
        code: Int,
        body: ResponseBody?
    ): Answer.Failure {
        val response = body?.string().fromJson<ServerErrorResponse>()
            val message = response?.error?.message ?: "No message"
            return when (code) {
                400 -> Answer.Failure(
                    NoException(),
                    code = ErrorCode.AuthorizationError, //Todo заменить код ошибки
                    message = response?.error?.detail ?: message
                )
                401 -> Answer.Failure(
                    NoException(),
                    code = ErrorCode.AuthorizationError,
                    message = response?.error?.detail ?: message
                )
                404 -> Answer.Failure(
                    NoException(),
                    code = ErrorCode.ExternalError,
                    message = message
                )
                else -> Answer.Failure(
                    NoException(),
                    ErrorCode.ExternalError,
                    message
                )
            }
    }
}

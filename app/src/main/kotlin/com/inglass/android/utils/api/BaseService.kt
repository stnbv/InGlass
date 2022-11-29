package com.inglass.android.utils.api

import com.inglass.android.data.remote.services.ServerErrorResponse
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.ErrorCode
import com.inglass.android.utils.api.core.NoException
import com.inglass.android.utils.helpers.fromJson
import okhttp3.ResponseBody
import retrofit2.Response

abstract class BaseService() {

    protected suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): Answer<T> {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (e: Exception) {
            return Answer.failure(ex = e, code = ErrorCode.InternalError, errorCodeNumber = 0)
        }
        return if (!response.isSuccessful) {
            Answer.failure(parseError(response.code(), response.errorBody()))
        } else {
            if (response.body() == null) {
                Answer.failure(ErrorCode.EmptyResponseError, errorCodeNumber = 0)
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
        val message = response?.message ?: "No message"
        val errorCodeNumber = response?.errorCode ?: 0

        return when (code) {
            400 -> Answer.Failure(
                NoException(),
                code = ErrorCode.Incorrect,
                message = message,
                errorCodeNumber = errorCodeNumber
            )
            401 -> {

                Answer.Failure(
                    NoException(),
                    code = ErrorCode.AuthorizationError,
                    message = message,
                    errorCodeNumber = errorCodeNumber
                )
            }
            404 -> Answer.Failure(
                NoException(),
                code = ErrorCode.ExternalError,
                message = message,
                errorCodeNumber = errorCodeNumber
            )
            500 -> Answer.Failure(
                NoException(),
                code = ErrorCode.ExternalError,
                message = "Internal server error",
                errorCodeNumber = errorCodeNumber
            )
            else -> Answer.Failure(
                NoException(),
                ErrorCode.InternalError,
                message,
                errorCodeNumber = errorCodeNumber
            )
        }
    }
}

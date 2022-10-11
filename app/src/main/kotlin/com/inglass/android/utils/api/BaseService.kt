package com.inglass.android.utils.api

import com.inglass.android.data.remote.services.ServerErrorResponse
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.ErrorCode
import com.inglass.android.utils.api.core.NoException
import com.inglass.android.utils.helpers.fromJson
import okhttp3.ResponseBody
import retrofit2.Response

const val TIME_NOT_PASSED_ERROR_CODE = 425

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

        if (response != null) {
            val message = response.error?.message ?: "No message"
            return when (code) {
                401 -> Answer.Failure(
                    NoException(),
                    code = ErrorCode.AuthorizationError,
                    message = response.error?.detail ?: message
                )
                404 -> Answer.Failure(
                    NoException(),
                    code = ErrorCode.RecordNotFoundError,
                    message = message
                )
                403 -> Answer.Failure(
                    NoException(),
                    code = ErrorCode.CPContainsMedicalConsultation,
                    message = message
                )
                405 -> Answer.Failure(
                    NoException(),
                    code = ErrorCode.AllAttemptsUsedError,
                    message = message
                )
                422 -> Answer.Failure(
                    NoException(),
                    code = ErrorCode.ExternalError,
                    message = message
                )
                TIME_NOT_PASSED_ERROR_CODE -> Answer.Failure(
                    NoException(),
                    code = ErrorCode.TimeNotPassedError,
                    message = response.error?.meta?.timeToNextAttempt.toString()
                )
                else -> Answer.Failure(
                    NoException(),
                    ErrorCode.ExternalError,
                    message
                )
            }
        }

        return Answer.Failure(
            NoException(),
            ErrorCode.InternalError,
            ""
        )
    }
}

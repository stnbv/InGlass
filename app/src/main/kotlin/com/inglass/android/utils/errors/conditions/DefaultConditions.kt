package com.inglass.android.utils.errors.conditions

import androidx.annotation.StringRes
import com.inglass.android.R
import com.inglass.android.utils.ErrorWrapper
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.Answer.Failure
import com.inglass.android.utils.api.core.ErrorCode

class LoginAndPassCondition(@StringRes private val errorString: Int = R.string.error_login_or_pass) :
    ICondition<String> {
    override fun validate(data: String): ErrorWrapper {
        val valid = data.isNotEmpty()
        return if (valid) ErrorWrapper.None else ErrorWrapper.ResourceText(errorString)
    }
}

class NetworkConditionMessagesLogin(
    private val errors: Map<ErrorCode, Int>,
    @StringRes private val defaultText: Int = R.string.error_internal_error,
    private val useServerMessage: Boolean = false,
) : ICondition<Failure?> {
    override fun validate(data: Answer.Failure?): ErrorWrapper {
        val errorCode = errors.keys.find { it == data?.code }
        return errorCode?.let {
            if (useServerMessage && data?.message?.isNotEmpty() == true) {
                ErrorWrapper.StringText(data.message)
            } else {
                errors[errorCode]?.let {
                    ErrorWrapper.ResourceText(it)
                }
            }
        } ?: if (data == null) ErrorWrapper.ResourceText(defaultText) else ErrorWrapper.StringText(data.message)
    }
}

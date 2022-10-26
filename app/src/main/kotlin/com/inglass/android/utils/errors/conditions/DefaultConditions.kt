package com.inglass.android.utils.errors.conditions

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.inglass.android.R
import com.inglass.android.utils.ErrorWrapper
import com.inglass.android.utils.REGEX_EMAIL
import com.inglass.android.utils.REGEX_PASS
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.Answer.Failure
import com.inglass.android.utils.api.core.ErrorCode
import com.inglass.android.utils.api.core.ErrorCode.InternalError
import com.inglass.android.utils.ui.Gender.Default

const val ERR_MIN_PASS_LENGTH = 8
const val ERR_MAX_PASS_LENGTH = 40
const val ERR_MAX_LOGIN_LENGTH = 64
const val ERR_MAX_DOMEN_LENGTH = 253
const val ERR_SUB_DOMEN_LENGTH = 64
const val ERR_MAX_PHONE_LENGTH = 11

const val ERR_SMS_CODE_LENGTH = 5

class LoginAndPassCondition(@StringRes private val errorString: Int = R.string.error_login_or_pass) :
    ICondition<String> {
    override fun validate(data: String): ErrorWrapper {
        val valid = data.isNotEmpty()
        return if (valid) ErrorWrapper.None else ErrorWrapper.ResourceText(errorString)
    }
}

class PhoneCondition(@StringRes private val stringId: Int = R.string.error_login_or_pass) : ICondition<String> {
    override fun validate(data: String): ErrorWrapper {
        val phone = data.fold("") { phone, currentChar -> if (currentChar in '0'..'9') "$phone$currentChar" else phone }
        val valid = phone.length == ERR_MAX_PHONE_LENGTH
        return if (valid) ErrorWrapper.None else ErrorWrapper.ResourceText(stringId)
    }
}

class PassCondition(@StringRes private val errorString: Int = R.string.error_login_or_pass) : ICondition<String> {
    // TODO посмотреть как можно оптимизировать REGEX_PASS и учитывает ли это отлов смайлов (Gerasimets / 24.11.2021)
    override fun validate(data: String): ErrorWrapper {
        val valid =
            !data.contains(REGEX_PASS.toRegex()) && data.length in ERR_MIN_PASS_LENGTH..ERR_MAX_PASS_LENGTH
        return if (valid) ErrorWrapper.None else ErrorWrapper.ResourceText(errorString)
    }
}

// Тут только ошибочное состояние
class NetworkCondition(
    @StringRes private val errorMessage: Int = R.string.error_external_error,
    private val useServerMessage: Boolean = false
) :
    ICondition<Failure?> {
    override fun validate(data: Failure?): ErrorWrapper {
        //TODO when по кодам ошибок от api (Gerasimets / 24.11.2021)
        return when (data?.code) {
            InternalError -> ErrorWrapper.ResourceText(R.string.error_internal_error)
            else -> {
                if (useServerMessage && data?.message?.isNotEmpty() == true) {
                    ErrorWrapper.StringText(data.message)
                } else {
                    ErrorWrapper.ResourceText(errorMessage)
                }
            }
        }
    }
}

class RepeatedCondition(
    private val password: MutableLiveData<String>,
    @StringRes private val errorString: Int
) : ICondition<String> {
    override fun validate(data: String): ErrorWrapper {
        val valid = password.value?.let { it == data } ?: false
        return if (valid) ErrorWrapper.None else ErrorWrapper.ResourceText(errorString)
    }
}

class EmailCondition : ICondition<String> {
    //TODO проверить учет длинны поддоменов от 1 до 64 (Gerasimets / 24.11.2021)
    override fun validate(data: String): ErrorWrapper {
        val valid =
            data.contains(Regex(REGEX_EMAIL)) && data.length <= ERR_MAX_LOGIN_LENGTH + ERR_MAX_DOMEN_LENGTH
        return if (valid) ErrorWrapper.None else ErrorWrapper.ResourceText(R.string.error_format_email)
    }
}

class RequiredCondition<T>(@StringRes private val stringId: Int) : ICondition<T> {
    override fun validate(data: T): ErrorWrapper {
        val valid = data?.let {
            if (it is CharSequence) it.isNotEmpty() else true
        } ?: false
        return if (valid) ErrorWrapper.None else ErrorWrapper.ResourceText(stringId)
    }
}

class GenderCondition(@StringRes private val stringId: Int) : ICondition<String> {
    override fun validate(data: String): ErrorWrapper {
        return when (data) {
            Default.gender -> ErrorWrapper.ResourceText(stringId)
            else -> ErrorWrapper.None
        }
    }
}

class MinLengthCondition(@StringRes private val stringId: Int, private val minLength: Int) : ICondition<String> {
    override fun validate(data: String): ErrorWrapper {
        return if (data.length >= minLength) ErrorWrapper.None else ErrorWrapper.ResourceText(stringId)
    }
}

class NetworkConditionMessages(
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
        } ?: ErrorWrapper.ResourceText(defaultText)
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

class PhoneByMaskCondition(
    var mask: MutableLiveData<String>?,
    @StringRes private val stringId: Int = R.string.error_login_or_pass
) : ICondition<String> {
    override fun validate(data: String): ErrorWrapper {
        val length = mask?.value?.filter { it == '_' }?.length ?: 0
        val phoneNumber = data.filter { it.isDigit() }
        return if (phoneNumber.find { it !in '0'..'9' } == null && phoneNumber.length >= length && phoneNumber.isNotEmpty()) {
            ErrorWrapper.None
        } else {
            ErrorWrapper.ResourceText(stringId)
        }
    }
}

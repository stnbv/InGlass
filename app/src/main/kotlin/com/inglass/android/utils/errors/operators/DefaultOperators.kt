package com.inglass.android.utils.errors.operators

import androidx.annotation.StringRes
import com.inglass.android.utils.ErrorWrapper

const val VALID_RES_ERR_SIZE = 1

class DefaultOperator : IOperator {
    override fun getError(validationResult: Set<ErrorWrapper>) =
        validationResult.find { it != ErrorWrapper.None } ?: ErrorWrapper.None
}

class DefaultMuxOperator(@StringRes private val stringId: Int) : IOperatorMux {
    override fun getError(validationResult: List<ErrorWrapper>): ErrorWrapper {
        val errors = validationResult.filter { it != ErrorWrapper.None }
        return when {
            errors.isEmpty() -> ErrorWrapper.None
            errors.size == VALID_RES_ERR_SIZE -> errors.first()
            else -> ErrorWrapper.ResourceText(stringId)
        }
    }
}

class FirstErrorMuxOperator : IOperatorMux {
    override fun getError(validationResult: List<ErrorWrapper>): ErrorWrapper {
        val errors = validationResult.filter { it != ErrorWrapper.None }
        return when {
            errors.isEmpty() -> ErrorWrapper.None
            else -> errors.first()
        }
    }
}

package com.inglass.android.utils.errors.operators

import com.inglass.android.utils.ErrorWrapper

class DefaultOperator : IOperator {
    override fun getError(validationResult: Set<ErrorWrapper>) =
        validationResult.find { it != ErrorWrapper.None } ?: ErrorWrapper.None
}

package com.inglass.android.utils.errors.operators

import com.inglass.android.utils.ErrorWrapper

// Тоже самое, что и IOperator, но с учетом того, что ошибки могут поовторяться по валидаторам
interface IOperatorMux {
    fun getError(validationResult: List<ErrorWrapper>): ErrorWrapper
}

package com.inglass.android.utils.errors.validators

import com.inglass.android.utils.ErrorWrapper

// Валидирует источник данных (source) по логике правил (Conditions). Итоговая ошибка вычисляется через operator
interface IValidator {
    fun validation()
    fun getState(): ErrorWrapper
}

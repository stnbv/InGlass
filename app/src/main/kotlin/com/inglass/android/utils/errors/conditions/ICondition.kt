package com.inglass.android.utils.errors.conditions

import com.inglass.android.utils.ErrorWrapper

// Условие валидации.
interface ICondition<T> {
    fun validate(data: T): ErrorWrapper
}

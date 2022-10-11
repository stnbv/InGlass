package com.inglass.android.utils.errors.observers

import com.inglass.android.utils.ErrorWrapper

// Подписчик на изменение состояние валидатора
interface IObserveValidate {
    fun observe(state: ErrorWrapper)
}

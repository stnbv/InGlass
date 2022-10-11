package com.inglass.android.utils.errors.validators

import com.inglass.android.utils.ErrorWrapper
import com.inglass.android.utils.errors.observers.IObserveValidate
import com.inglass.android.utils.errors.operators.IOperatorMux

// То же самое, что и Validator, но приводит к общей ошибке Validator-ы
class MuxValidator(private var operator: IOperatorMux) {

    // Вызывает метод validation у переданных валидаторов
    fun requestAllValidator() {
        validators.forEach { it.validation() }
    }

    fun validate() {
        state = operator.getError(validators.map { it.getState() })
        observer?.observe(state)
    }

    fun setOperator(operator: IOperatorMux) {
        this.operator = operator
    }

    fun addValidator(validator: IValidator) {
        validators.add(validator)
    }

    fun removeValidator(validator: IValidator) {
        validators.remove(validator)
    }

    fun setObserver(observer: IObserveValidate) {
        this.observer = observer
    }

    fun setObserver(observer: (ErrorWrapper) -> Unit) {
        this.observer = object : IObserveValidate {
            override fun observe(state: ErrorWrapper) {
                observer(state)
            }
        }
    }

    fun getState() = state

    private var state: ErrorWrapper = ErrorWrapper.None
    private val validators: MutableList<IValidator> = mutableListOf()
    private var observer: IObserveValidate? = null
}

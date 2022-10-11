package com.inglass.android.utils.errors.validators

import androidx.lifecycle.MutableLiveData
import com.inglass.android.utils.ErrorWrapper
import com.inglass.android.utils.errors.conditions.ICondition
import com.inglass.android.utils.errors.observers.IObserveValidate
import com.inglass.android.utils.errors.operators.IOperator
import com.inglass.android.utils.errors.operators.Operator

/**
 * Validator проверяет source по набору правил (conditions).
 * Результат проверки - набор ошибок (validationResult).
 * operator - сущность, которая превращает validationResult в 1 общую ошибку.
 * 1 общая ошибка - state валидатора, по-умолчанию - ErrorWrapper.None
 * */

class Validator<T>(
    private val source: MutableLiveData<T>,
    private var operator: IOperator,
    private val errorText: Int? = null
) : IValidator {

    constructor(
        source: MutableLiveData<T>,
        operator: Operator = Operator.Default,
        nullStrErrId: Int? = null
    ) : this(source, operator.operator, nullStrErrId)

    // Observer состояния валидатора
    private var observer: IObserveValidate? = null

    // набор условий по которым происходит валидация
    private val conditions: MutableSet<ICondition<T>> = mutableSetOf()

    // нвбор результатов валидации
    private val validationResult: MutableSet<ErrorWrapper> = mutableSetOf()

    // Результирующая ошибка валидации
    private var state: ErrorWrapper = ErrorWrapper.None

    // обновляет state текущего валидатора (для отображения ошибки по всем полям, muxValidator
    override fun validation() {
        validationResult.clear()

        source.value?.let { source ->
            conditions.forEach { condition -> validationResult.add(condition.validate(source)) }
        } ?: errorText?.let { validationResult.add(ErrorWrapper.ResourceText(it)) }

        state = operator.getError(validationResult)
        observer?.observe(state)
    }

    // добавить правило
    fun addCondition(condition: ICondition<T>) {
        conditions.add(condition)
    }

    // удалить правило
    fun removeCondition(condition: ICondition<T>) {
        conditions.remove(condition)
    }

    // изменить оператор
    fun changeOperator(operator: IOperator) {
        this.operator = operator
    }

    // установить observer
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

    override fun getState() = state
}

package com.inglass.android.utils.api.core

import com.inglass.android.utils.api.core.Answer.Failure
import kotlinx.coroutines.delay

@Suppress("unchecked_cast")
class Answer<out T>(val value: Any?) {

    val isSuccess: Boolean
        get() = value !is Failure

    val isFailure: Boolean
        get() = value is Failure

    fun getOrNull(): T? = when {
        isFailure -> null
        else -> value as T
    }

    fun errorOrNull(): Failure? = when (value) {
        is Failure -> value
        else -> null
    }

    override fun toString(): String =
        when (value) {
            is Failure -> value.toString()
            else -> "Success($value)"
        }

    companion object {
        fun <T> success(value: T): Answer<T> = Answer(value)

        fun <T> failure(ex: Throwable, code: ErrorCode, message: String = ""): Answer<T> =
            Answer(createFailure(ex, code, message))

        fun <T> failure(code: ErrorCode, message: String = ""): Answer<T> =
            Answer(createFailure(code, message))

        fun <T> failure(error: Failure?): Answer<T> = Answer(error)
    }

    class Failure(val exception: Throwable, val code: ErrorCode, val message: String) {
        override fun equals(other: Any?): Boolean = other is Failure && exception == other.exception
        override fun hashCode(): Int = exception.hashCode()
        override fun toString(): String = "Failure($exception) code(${code}) message(${message})"
    }
}

private fun createFailure(exception: Throwable, code: ErrorCode, message: String): Any =
    Failure(exception, code, message)

private fun createFailure(code: ErrorCode, message: String): Any = Failure(NoException(), code, message)

@Suppress("unchecked_cast")
inline fun <R, T> Answer<T>.map(transform: (value: T) -> R): Answer<R> {
    return when {
        isSuccess -> Answer.success(transform(value as T))
        else -> Answer(value)
    }
}

inline fun <T> Answer<T>.onFailure(action: (error: Failure) -> Unit): Answer<T> {
    errorOrNull()?.let { action(it) }
    return this
}

@Suppress("unchecked_cast")
inline fun <T> Answer<T>.onSuccess(action: (value: T) -> Unit): Answer<T> {
    if (isSuccess) action(value as T)
    return this
}

fun Answer<*>.throwOnFailure() {
    if (value is Failure) throw value.exception
}
suspend fun <T> retry(count: Int, operation: suspend () -> Answer<T>): Answer<T> {
    check(count > 0)

    var lastAnswer: Answer<T>? = null
    repeat(count) {
        val result = operation()
        if (result.isSuccess) return result
        delay(3000)
        lastAnswer = result
    }
    return lastAnswer!!
}

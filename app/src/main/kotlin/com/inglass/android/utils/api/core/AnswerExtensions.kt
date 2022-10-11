package com.inglass.android.utils.api.core

import com.inglass.android.utils.api.core.Answer.Failure

inline fun <R, T> Answer<T>.mapResult(transform: (value: T) -> Answer<R>): Answer<R> {
    return fold(
        onSuccess = { transform(it) },
        onFailure = { Answer.failure(it.exception, it.code, it.message) }
    )
}

inline fun <R> Answer<R>.mapError(transform: (value: Failure) -> Answer<R>): Answer<R> {
    return fold(
        onSuccess = { Answer.success(it) },
        onFailure = { transform(it) }
    )
}

inline fun <T> Answer<T>.onAny(action: () -> Unit): Answer<T> {
    action()
    return this
}

inline fun <F, S, O> Answer<F>.concat(
    concat: Answer<S>,
    transform: (value: F, S) -> O
): Answer<O> {
    return mapResult { f ->
        concat.mapResult { s ->
            Answer.success(transform(f, s))
        }
    }
}

fun <R> Answer<List<R>>.concat(concat: Answer<List<R>>): Answer<List<R>> {
    return mapResult { f ->
        concat.mapResult { s ->
            Answer.success(f + s)
        }
    }
}

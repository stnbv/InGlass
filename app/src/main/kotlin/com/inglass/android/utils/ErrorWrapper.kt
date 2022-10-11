package com.inglass.android.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData

sealed class ErrorWrapper {
    class ResourceText(@StringRes val errorText: Int) : ErrorWrapper() {
        override fun getText(context: Context) = context.getString(this.errorText)
        override val isThereError: Boolean = true
    }

    class StringText(val errorText: String) : ErrorWrapper() {
        override fun getText(context: Context) = this.errorText
        override val isThereError: Boolean = true
    }

    object None : ErrorWrapper() {
        override fun getText(context: Context) = String()
        override val isThereError: Boolean = false
    }

    abstract fun getText(context: Context): String

    abstract val isThereError: Boolean

    fun LiveData<ErrorWrapper>.isThereError(): Boolean = value?.isThereError ?: false
}

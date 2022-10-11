package com.inglass.android.utils.helpers

import com.google.gson.Gson
import timber.log.Timber

class GsonHelper {
    companion object {
        val gson = Gson()
    }
}

inline fun <reified T> String?.fromJson(): T? {
    return try {
        GsonHelper.gson.fromJson(this, T::class.java)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun <T> T.toJson(): String? {
    return try {
        GsonHelper.gson.toJson(this)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

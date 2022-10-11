package com.inglass.android.utils.analitics

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings

class DeviceInfo(private val context: Context) {

    val deviceBrand: String
        get() = Build.BRAND

    val deviceModel: String
        get() = Build.MODEL

    val deviceId: String
        @SuppressLint("HardwareIds")
        get() {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }
}

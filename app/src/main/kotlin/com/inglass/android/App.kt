package com.inglass.android

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.inglass.android.data.local.db.entities.ScanResult
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    companion object {
        private lateinit var instance: App
        var scanResSet: MutableSet<String> = mutableSetOf<String>()
        var currentOperation: String = "Резка"
        var scanData = MutableLiveData<ScanResult>()

        fun get(): App {
            return instance
        }
    }
}

package com.inglass.android.utils.analitics.firebase

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.inglass.android.utils.analitics.AnalyticEvent
import com.inglass.android.utils.analitics.AnalyticsPerformer

class FirebaseAnalyticPerformer(val context: Context) : AnalyticsPerformer {

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(context)
    }

    override fun sendAnaliticsEvent(event: AnalyticEvent) {
        val bundle = Bundle()
        bundle.putString("UUID", event.deviceId)

        when (event.label) {
            is String -> bundle.putString(event.action, event.label)
            is Int -> bundle.putInt(event.action, event.label)
            else -> bundle.putString(event.action, event.label.toString())
        }

        firebaseAnalytics.logEvent(event.category, bundle)
    }
}

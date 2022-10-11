package com.inglass.android.utils.analitics

import android.content.Context
import com.inglass.android.utils.analitics.firebase.FirebaseAnalyticPerformer
import com.inglass.android.utils.analitics.routers.AuthAnalyticRouter

class AnalyticsManager(
    private val context: Context,
    private val firebaseAnalyticPerformer: FirebaseAnalyticPerformer,
    private val authAnalyticRouter: AuthAnalyticRouter,
    private val deviceInfo: DeviceInfo
) {

    private val analytics by lazy {
        listOfNotNull(
            if (isGSMAvailable(context)) firebaseAnalyticPerformer else null
            //TODO Можно добавить другие аналитические системы
        )
    }

    fun handleAnalyticMessage(message: AnalyticMessage) {
        reportEvent(authAnalyticRouter.handleAnalyticMessage(message))
    }

    private fun reportEvent(event: AnalyticEvent?) {
        if (event == null) return
        event.deviceId = deviceInfo.deviceId
        analytics.forEach { performer ->
            performer.sendAnaliticsEvent(event)
        }
    }

    private fun isGSMAvailable(context: Context) =
        com.google.android.gms.common.GoogleApiAvailabilityLight.getInstance()
            .isGooglePlayServicesAvailable(context) == com.google.android.gms.common.ConnectionResult.SUCCESS
}

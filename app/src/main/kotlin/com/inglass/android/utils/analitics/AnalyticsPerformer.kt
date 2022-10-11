package com.inglass.android.utils.analitics

interface AnalyticsPerformer {
    fun sendAnaliticsEvent(event: AnalyticEvent)
}

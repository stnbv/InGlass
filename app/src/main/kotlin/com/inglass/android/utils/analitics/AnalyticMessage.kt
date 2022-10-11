package com.inglass.android.utils.analitics

sealed interface AnalyticMessage {
    //Эвенты авторизации
    object AnalyticsLogInEvent : AnalyticMessage
}

package com.inglass.android.utils.analitics

interface AnalyticMessageHandler {
    fun handleAnalyticMessage(message: AnalyticMessage): AnalyticEvent?
}

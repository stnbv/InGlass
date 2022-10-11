package com.inglass.android.utils.analitics

data class AnalyticEvent(
    val category: String,
    val action: String,
    val label: Any,
    var deviceId: String = ""
)

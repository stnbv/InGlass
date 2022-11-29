package com.inglass.android.domain.models

data class SecondaryScanInfo(
    val employeeId: String,
    val participationRate: Float,
    val helpers: List<Helper>
)

package com.inglass.android.domain.models

data class CompanionsModel(
    val id: String,
    val participationRate: Float
)

data class CompanionsFullInfoModel(
    val id: String,
    val name: String,
    val participationRate: Float
)
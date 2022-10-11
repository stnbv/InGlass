package com.inglass.android.domain.models.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConfirmPhoneModel(
    val timeToNextAttempt: Long
) : Parcelable

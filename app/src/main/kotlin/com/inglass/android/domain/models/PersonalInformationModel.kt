package com.inglass.android.domain.models

import android.os.Parcelable
import com.inglass.android.utils.ui.Gender
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonalInformationModel(
    val id: String,
    val avatar: String?,
    val avatarId: Int?,
    val userName: String,
    val email: String,
    val birthday: String,
    val gender: Gender,
    val phoneNumber: Long,
) : Parcelable

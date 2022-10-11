package com.inglass.android.data.remote.responses

import com.google.gson.annotations.SerializedName
import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.utils.ui.Gender
import com.inglass.android.utils.ui.Gender.Default
import com.inglass.android.utils.ui.Gender.Female
import com.inglass.android.utils.ui.Gender.Male

data class PersonalInformationResponse(
    @SerializedName("data") val data: PersonalInformationData
)

data class PersonalInformationData(
    @SerializedName("id") val id: String,
    @SerializedName("type") val type: String, // profile
    @SerializedName("attributes") val personalInformationAttributes: PersonalInformationAttributes
)

data class PersonalInformationAttributes(
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("avatar_id") val avatarId: Int?,
    @SerializedName("name") val userName: String,
    @SerializedName("email") val email: String,
    @SerializedName("birthday") val birthday: String, //TODO будет изменено на Long (Artem 14/12/2021)
    @SerializedName("gender") val gender: String,
    @SerializedName("phone_number") val phoneNumber: Long,
)

fun PersonalInformationResponse.toPersonalInformationModel() = PersonalInformationModel(
    id = data.id,
    avatar = data.personalInformationAttributes.avatar,
    userName = data.personalInformationAttributes.userName,
    email = data.personalInformationAttributes.email,
    birthday = data.personalInformationAttributes.birthday,
    gender = setGender(data.personalInformationAttributes.gender),
    phoneNumber = data.personalInformationAttributes.phoneNumber,
    avatarId = data.personalInformationAttributes.avatarId
)

private fun setGender(gender: String): Gender {
    return when (gender) {
        "male" -> Male
        "female" -> Female
        else -> Default
    }
}

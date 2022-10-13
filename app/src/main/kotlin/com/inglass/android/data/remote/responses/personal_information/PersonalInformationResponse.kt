package com.inglass.android.data.remote.responses.personal_information

import com.google.gson.annotations.SerializedName
import com.inglass.android.domain.models.PersonalInformationModel

data class PersonalInformationResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("middle_name") val middleName: String?,
    @SerializedName("photo") val photo: String?,
    @SerializedName("available_operations") val availableOperations: List<Int>
)

fun PersonalInformationResponse.toModel() = PersonalInformationModel(
    id = id,
    email = email,
    firstName = firstName,
    lastName = lastName ?: "",
    middleName = middleName ?: "",
    photo = photo ?: "",
    availableOperations = availableOperations
)

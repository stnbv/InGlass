package com.inglass.android.domain.models

data class PersonalInformationModel(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val middleName: String,
    val photo: String,
    val availableOperations: List<String>
) {
    val fullName = "$lastName $firstName $middleName".trim()
}
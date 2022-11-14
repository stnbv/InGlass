package com.inglass.android.data.remote.responses.reference_book

import com.google.gson.annotations.SerializedName
import com.inglass.android.domain.models.EmployeeAndOperationModel
import com.inglass.android.domain.models.ReferenceBookModel

data class ReferenceBookResponse(
    @SerializedName("employee") val employees: List<Employees>?,
    @SerializedName("operations") val operations: List<Operations>?
)

data class Employees(
    val id: String,
    val name: String
)

data class Operations(
    val id: String,
    val name: String
)

fun ReferenceBookResponse.toModel() = ReferenceBookModel(
    employees = employees?.map {
        EmployeeAndOperationModel(it.id, it.name)
    } ?: emptyList(),
    operations = operations?.map {
        EmployeeAndOperationModel(it.id, it.name)
    } ?: emptyList()
)

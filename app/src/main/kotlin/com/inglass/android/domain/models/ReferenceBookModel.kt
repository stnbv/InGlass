package com.inglass.android.domain.models

data class ReferenceBookModel(
    val employees: List<EmployeeAndOperationModel>,
    val operations: List<EmployeeAndOperationModel>
)

data class EmployeeAndOperationModel(
    val id: Int,
    val name: String
)

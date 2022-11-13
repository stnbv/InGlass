package com.inglass.android.domain.repository.interfaces

import com.inglass.android.data.local.db.entities.Employee
import com.inglass.android.data.local.db.entities.Operation
import com.inglass.android.domain.models.ReferenceBookModel
import com.inglass.android.utils.api.core.Answer

interface IReferenceBookRepository {

    suspend fun getReferenceBook(): Answer<ReferenceBookModel>

    suspend fun saveOperations(operations: List<Operation>)

    suspend fun getOperations(): List<Operation>

    suspend fun saveEmployee(items: List<Employee>)

}

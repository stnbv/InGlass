package com.inglass.android.domain.repository

import com.inglass.android.data.local.db.dao.EmployeeDao
import com.inglass.android.data.local.db.dao.OperationsDao
import com.inglass.android.data.local.db.entities.Employee
import com.inglass.android.data.local.db.entities.Operation
import com.inglass.android.data.remote.responses.reference_book.ReferenceBookResponse
import com.inglass.android.data.remote.responses.reference_book.toModel
import com.inglass.android.data.remote.services.reference_book.IReferenceBookService
import com.inglass.android.domain.models.ReferenceBookModel
import com.inglass.android.domain.repository.interfaces.IReferenceBookRepository
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.map

class ReferenceBookRepository(
    private val service: IReferenceBookService,
    private val operationsDao: OperationsDao,
    private val employeeDao: EmployeeDao
) : IReferenceBookRepository {

    override suspend fun getReferenceBook(): Answer<ReferenceBookModel> {
        return service.getReferenceBook().map(ReferenceBookResponse::toModel)
    }

    override suspend fun saveOperations(operations: List<Operation>) {
        operationsDao.insertOperations(operations)
    }

    override suspend fun getOperations(): List<Operation> {
        return operationsDao.getOperations()
    }

    override suspend fun saveEmployee(items: List<Employee>) {
        employeeDao.insertEmployee(items)
    }
}

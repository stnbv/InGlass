package com.inglass.android.domain.usecase.companions

import com.inglass.android.domain.repository.interfaces.ICompanionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCompanionsUseCase(private val repository: ICompanionsRepository) {

    suspend operator fun invoke() {
        withContext(Dispatchers.IO) {
            repository.getCompanions()
        }
    }
}
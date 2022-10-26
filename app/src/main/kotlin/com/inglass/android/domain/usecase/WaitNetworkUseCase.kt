package com.inglass.android.domain.usecase

import com.inglass.android.domain.repository.interfaces.INetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WaitNetworkUseCase(private val repository: INetworkRepository) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        repository.waitAvailableNetwork()
    }
}

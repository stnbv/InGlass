package com.inglass.android.domain.usecase.auth

import com.inglass.android.utils.api.core.Answer
import com.inglass.android.domain.models.auth.AuthModel
import com.inglass.android.domain.repository.interfaces.IAuthRepository
import com.inglass.android.domain.usecase.auth.LogInUseCase.Params
import com.inglass.android.domain.usecase.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogInUseCase(private val repository: IAuthRepository) : UseCase<Params, AuthModel>() {

    override suspend operator fun invoke(params: Params): Answer<AuthModel> {
        return withContext(Dispatchers.IO) {
            repository.logIn(params.phoneNumber, params.password)
        }
    }

    class Params(
        val phoneNumber: Long,
        val password: String
    )
}

package com.inglass.android.domain.usecase.auth

import com.inglass.android.domain.repository.interfaces.IAuthRepository
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.UseCase
import com.inglass.android.domain.usecase.auth.LogInUseCase.Params
import com.inglass.android.utils.api.core.Answer
import com.inglass.android.utils.api.core.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogInUseCase(
    private val repository: IAuthRepository,
    private val preferences: IPreferencesRepository
) : UseCase<Params, Unit>() {

    override suspend operator fun invoke(params: Params): Answer<Unit> {
        return withContext(Dispatchers.IO) {
            val token = repository.logIn(params.login, params.password)
            token.map {
                preferences.token = it.token
            }
        }
    }

    class Params(
        val login: String,
        val password: String
    )
}

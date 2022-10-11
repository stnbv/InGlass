package com.inglass.android.di.usecases

import com.inglass.android.domain.repository.interfaces.IAuthRepository
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.auth.AuthStateUseCase
import com.inglass.android.domain.usecase.auth.LogInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCasesModule {

    @Provides
    fun provideGetAuthStateUseCase(iPreferencesRepository: IPreferencesRepository) = AuthStateUseCase(iPreferencesRepository)


    @Provides
    fun provideAuthUseCase(iAuthRepository: IAuthRepository) = LogInUseCase(iAuthRepository)

}

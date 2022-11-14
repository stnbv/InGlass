package com.inglass.android.di.usecases

import com.inglass.android.domain.repository.interfaces.IMakeOperationRepository
import com.inglass.android.domain.usecase.scanning.MakeOperationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MakeOperationUseCaseModule {

    @Provides
    fun provideMakeOperationUseCase(makeOperationRepository: IMakeOperationRepository) =
        MakeOperationUseCase(makeOperationRepository)

}

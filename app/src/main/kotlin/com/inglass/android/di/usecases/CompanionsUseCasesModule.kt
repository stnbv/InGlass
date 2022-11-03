package com.inglass.android.di.usecases

import com.inglass.android.domain.repository.interfaces.ICompanionsRepository
import com.inglass.android.domain.usecase.companions.GetCompanionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CompanionsUseCasesModule {

    @Provides
    fun provideCompanionsUseCase(
        companionsRepository: ICompanionsRepository
    ) = GetCompanionsUseCase(companionsRepository)

}
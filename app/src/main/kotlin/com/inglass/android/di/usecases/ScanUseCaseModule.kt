package com.inglass.android.di.usecases

import com.inglass.android.domain.repository.interfaces.ICompanionsRepository
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
import com.inglass.android.domain.usecase.scanning.GetSecondaryScanInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ScanUseCaseModule {

    @Provides
    fun provideSecondaryScanInfo(
        iPreferencesRepository: IPreferencesRepository,
        companionsRepository: ICompanionsRepository,
        scanResultsRepository: IScanResultsRepository
    ) = GetSecondaryScanInfoUseCase(iPreferencesRepository, companionsRepository, scanResultsRepository)

}

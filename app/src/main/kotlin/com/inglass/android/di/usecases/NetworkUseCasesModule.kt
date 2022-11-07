package com.inglass.android.di.usecases

import com.inglass.android.domain.repository.interfaces.INetworkRepository
import com.inglass.android.domain.usecase.WaitNetworkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkUseCasesModule {

    @Provides
    fun provideNetworkUseCase(iNetworkRepository: INetworkRepository) =
        WaitNetworkUseCase(iNetworkRepository)

}

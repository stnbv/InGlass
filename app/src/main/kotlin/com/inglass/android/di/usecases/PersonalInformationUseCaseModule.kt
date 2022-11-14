package com.inglass.android.di.usecases

import com.inglass.android.domain.repository.interfaces.IPersonalInformationRepository
import com.inglass.android.domain.usecase.personal_information.GetPersonalInformationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object PersonalInformationUseCaseModule {

    @Provides
    fun providePersonalInformationUseCase(
        personalInformationRepository: IPersonalInformationRepository
    ) = GetPersonalInformationUseCase(personalInformationRepository)

}

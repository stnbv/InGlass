package com.inglass.android.di

import android.content.Context
import com.inglass.android.data.remote.services.auth.IAuthService
import com.inglass.android.data.remote.services.personal_information.IPersonalInformationService
import com.inglass.android.domain.repository.AuthRepository
import com.inglass.android.domain.repository.PersonalInformationRepository
import com.inglass.android.domain.repository.PreferencesRepository
import com.inglass.android.domain.repository.interfaces.IAuthRepository
import com.inglass.android.domain.repository.interfaces.IPersonalInformationRepository
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePreferencesRepository(@ApplicationContext context: Context): IPreferencesRepository =
        PreferencesRepository(context)

    @Provides
    fun provideAuthRepository(service: IAuthService): IAuthRepository = AuthRepository(service)

    @Provides
    fun providePersonalInformationRepository(
        service: IPersonalInformationService,
        prefRepository: IPreferencesRepository
    ): IPersonalInformationRepository = PersonalInformationRepository(service, prefRepository)

}

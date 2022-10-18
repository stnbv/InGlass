package com.inglass.android.di

import android.content.Context
import com.inglass.android.data.remote.services.auth.IAuthService
import com.inglass.android.data.remote.services.make_operation.IMakeOperationService
import com.inglass.android.data.remote.services.personal_information.IPersonalInformationService
import com.inglass.android.data.remote.services.reference_book.IReferenceBookService
import com.inglass.android.domain.repository.AuthRepository
import com.inglass.android.domain.repository.MakeOperationRepository
import com.inglass.android.domain.repository.PersonalInformationRepository
import com.inglass.android.domain.repository.PreferencesRepository
import com.inglass.android.domain.repository.ReferenceBookRepository
import com.inglass.android.domain.repository.ScanResultsRepository
import com.inglass.android.domain.repository.interfaces.IAuthRepository
import com.inglass.android.domain.repository.interfaces.IMakeOperationRepository
import com.inglass.android.domain.repository.interfaces.IPersonalInformationRepository
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.repository.interfaces.IReferenceBookRepository
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
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

    @Provides
    fun provideReferenceBookRepository(service: IReferenceBookService): IReferenceBookRepository =
        ReferenceBookRepository(service)

    @Provides
    fun provideMakeOperationRepository(service: IMakeOperationService): IMakeOperationRepository =
        MakeOperationRepository(service)

    @Provides
    @Singleton
    fun provideScanResultsRepository(): IScanResultsRepository =
        ScanResultsRepository()

}

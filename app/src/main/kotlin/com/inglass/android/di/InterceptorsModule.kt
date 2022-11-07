package com.inglass.android.di

import com.inglass.android.data.remote.interceptors.AuthTokenInterceptor
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.auth.LogInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.logging.HttpLoggingInterceptor

@Module
@InstallIn(SingletonComponent::class)
object InterceptorsModule {

    @Provides
    @Singleton
    fun provideAuthTokenInterceptor(logInUseCase: LogInUseCase, preferencesRepository: IPreferencesRepository) =
        AuthTokenInterceptor(logInUseCase, preferencesRepository)

    @Provides
    @Singleton
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

}

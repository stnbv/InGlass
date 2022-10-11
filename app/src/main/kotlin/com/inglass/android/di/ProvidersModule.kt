package com.inglass.android.di

import android.content.Context

import com.inglass.android.utils.provider.ConnectivityStatusProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvidersModule {

    @Provides
    @Singleton
    fun provideConnectivityStatusProvider(@ApplicationContext context: Context) = ConnectivityStatusProvider(context)

}

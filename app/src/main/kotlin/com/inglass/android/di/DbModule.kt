package com.inglass.android.di

import android.content.Context
import com.inglass.android.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = AppDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideScanResultDao(appDatabase: AppDatabase) = appDatabase.scanResultsDao()

    @Provides
    @Singleton
    fun provideOperationsDao(appDatabase: AppDatabase) = appDatabase.operationsDao()

    @Provides
    @Singleton
    fun provideEmployeeDao(appDatabase: AppDatabase) = appDatabase.employeeDao()

    @Provides
    @Singleton
    fun provideUserHelpersDao(appDatabase: AppDatabase) = appDatabase.userHelpersDao()

}

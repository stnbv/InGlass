package com.inglass.android.di

import android.content.Context
import com.inglass.android.utils.analitics.AnalyticsManager
import com.inglass.android.utils.analitics.DeviceInfo
import com.inglass.android.utils.analitics.firebase.FirebaseAnalyticPerformer
import com.inglass.android.utils.analitics.routers.AuthAnalyticRouter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideFirebaseAnalyticPerformer(@ApplicationContext context: Context) = FirebaseAnalyticPerformer(context)

    @Provides
    @Singleton
    fun provideAuthAnalyticRouter() = AuthAnalyticRouter()

    @Provides
    @Singleton
    fun provideDeviceInfo(@ApplicationContext context: Context) = DeviceInfo(context)

    @Provides
    @Singleton
    fun provideAnalyticsManager(
        @ApplicationContext context: Context,
        firebaseAnalyticPerformer: FirebaseAnalyticPerformer,
        authAnalyticRouter: AuthAnalyticRouter,
        deviceInfo: DeviceInfo
    ) = AnalyticsManager(context, firebaseAnalyticPerformer, authAnalyticRouter, deviceInfo)
}

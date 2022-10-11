package com.inglass.android.di

import com.inglass.android.utils.cash_storage.CacheStorageImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.inglass.android.utils.cash_storage.CacheStorage

@Module
@InstallIn(SingletonComponent::class)
object StoreModule {

    @Provides
    @Singleton
    fun provideCashStore(): CacheStorage = CacheStorageImpl()
}

package com.inglass.android.di

import com.inglass.android.data.remote.api.IAuthApi
import com.inglass.android.data.remote.api.IPersonalInformationApi
import com.inglass.android.data.remote.interceptors.AuthTokenInterceptor
import com.inglass.android.data.remote.services.auth.AuthService
import com.inglass.android.data.remote.services.auth.IAuthService
import com.inglass.android.data.remote.services.personal_information.IPersonalInformationService
import com.inglass.android.data.remote.services.personal_information.PersonalInformationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.*
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TIMEOUT = 20L

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val HTTP_SCHEME = "https://"
    private const val API_PATH = "cosmetology.joy-dev.com/api/"

    @Provides
    @Singleton
    fun provideClient(authTokenInterceptor: AuthTokenInterceptor, httpLoggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().apply {
            followRedirects(false)
            followSslRedirects(false)
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            addInterceptor(authTokenInterceptor)
            addInterceptor(httpLoggingInterceptor) //Всегда должен быть последним
        }.build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("$HTTP_SCHEME$API_PATH")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideAuthApi(retrofit: Retrofit): IAuthApi = retrofit.create(IAuthApi::class.java)

    @Provides
    fun provideAuthService(api: IAuthApi): IAuthService = AuthService(api)

    @Provides
    fun providePersonalInformationApi(retrofit: Retrofit): IPersonalInformationApi =
        retrofit.create(IPersonalInformationApi::class.java)

    @Provides
    fun providePersonalInformationService(api: IPersonalInformationApi): IPersonalInformationService =
        PersonalInformationService(api)

}

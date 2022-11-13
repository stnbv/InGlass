package com.inglass.android.di

import com.inglass.android.BuildConfig
import com.inglass.android.data.remote.api.IAuthApi
import com.inglass.android.data.remote.api.ICompanionsApi
import com.inglass.android.data.remote.api.IMakeOperationApi
import com.inglass.android.data.remote.api.IPersonalInformationApi
import com.inglass.android.data.remote.api.IReferenceBooksApi
import com.inglass.android.data.remote.interceptors.AuthTokenInterceptor
import com.inglass.android.data.remote.interceptors.TokenAuthenticator
import com.inglass.android.data.remote.services.auth.AuthService
import com.inglass.android.data.remote.services.auth.IAuthService
import com.inglass.android.data.remote.services.companions.CompanionsService
import com.inglass.android.data.remote.services.companions.ICompanionsService
import com.inglass.android.data.remote.services.make_operation.IMakeOperationService
import com.inglass.android.data.remote.services.make_operation.MakeOperationService
import com.inglass.android.data.remote.services.personal_information.IPersonalInformationService
import com.inglass.android.data.remote.services.personal_information.PersonalInformationService
import com.inglass.android.data.remote.services.reference_book.IReferenceBookService
import com.inglass.android.data.remote.services.reference_book.ReferenceBookService
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.auth.LogInUseCase
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

    @Provides
    fun provideAuthenticator(
        preferencesRepository: IPreferencesRepository,
        logInUseCase: LogInUseCase
    ): TokenAuthenticator = TokenAuthenticator(preferencesRepository, logInUseCase)

    @Provides
    @Singleton
    fun provideClient(
        authTokenInterceptor: AuthTokenInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authenticator: TokenAuthenticator
    ) = OkHttpClient.Builder().apply {
        followRedirects(false)
        followSslRedirects(false)
        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        readTimeout(TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        authenticator(authenticator)
        addInterceptor(authTokenInterceptor)
        addInterceptor(httpLoggingInterceptor) //Всегда должен быть последним
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("${BuildConfig.BASE_URL}${BuildConfig.API_ENDPOINT}")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @AuthQualifier
    fun provideAuthClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        authTokenInterceptor: AuthTokenInterceptor
    ) =
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
    @AuthQualifier
    fun provideAuthRetrofit(@AuthQualifier client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("${BuildConfig.BASE_URL}${BuildConfig.API_ENDPOINT}")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideAuthApi(@AuthQualifier retrofit: Retrofit): IAuthApi = retrofit.create(IAuthApi::class.java)

    @Provides
    fun provideAuthService(api: IAuthApi): IAuthService = AuthService(api)

    @Provides
    fun providePersonalInformationApi(retrofit: Retrofit): IPersonalInformationApi =
        retrofit.create(IPersonalInformationApi::class.java)

    @Provides
    fun providePersonalInformationService(api: IPersonalInformationApi): IPersonalInformationService =
        PersonalInformationService(api)

    @Provides
    fun provideReferenceBookApi(retrofit: Retrofit): IReferenceBooksApi =
        retrofit.create(IReferenceBooksApi::class.java)

    @Provides
    fun provideReferenceBookService(api: IReferenceBooksApi): IReferenceBookService = ReferenceBookService(api)


    @Provides
    fun provideMakeOperationApi(retrofit: Retrofit): IMakeOperationApi = retrofit.create(IMakeOperationApi::class.java)

    @Provides
    fun provideMakeOperationService(api: IMakeOperationApi): IMakeOperationService = MakeOperationService(api)

    @Provides
    fun provideCompanionsApi(retrofit: Retrofit): ICompanionsApi = retrofit.create(ICompanionsApi::class.java)

    @Provides
    fun provideCompanionsService(api: ICompanionsApi): ICompanionsService = CompanionsService(api)

}

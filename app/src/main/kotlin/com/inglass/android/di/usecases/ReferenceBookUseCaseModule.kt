package com.inglass.android.di.usecases

import com.inglass.android.domain.repository.interfaces.IReferenceBookRepository
import com.inglass.android.domain.usecase.reference_book.GetReferenceBookUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent


@Module
@InstallIn(ViewModelComponent::class)
object ReferenceBookUseCaseModule {

    @Provides
    fun provideReferenceBookUseCase(
        referenceBookRepository: IReferenceBookRepository
    ) = GetReferenceBookUseCase(referenceBookRepository)

}
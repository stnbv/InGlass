package com.inglass.android.domain.usecase

import com.inglass.android.utils.api.core.Answer

abstract class UseCase<Input, Output> {
    abstract suspend operator fun invoke(params: Input): Answer<Output>
}

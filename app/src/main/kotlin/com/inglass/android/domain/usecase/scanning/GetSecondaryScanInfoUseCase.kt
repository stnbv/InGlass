package com.inglass.android.domain.usecase.scanning

import com.inglass.android.domain.models.Helper
import com.inglass.android.domain.models.SecondaryScanInfo
import com.inglass.android.domain.repository.interfaces.ICompanionsRepository
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSecondaryScanInfoUseCase(
    private val preferences: IPreferencesRepository,
    private val companionsRepository: ICompanionsRepository,
    private val scanResultsRepository: IScanResultsRepository
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        val employeeId = preferences.user?.id
        var helpersRateSum = 0F
        val helpers = mutableListOf<Helper>()
        companionsRepository.getCompanionsFullInfo().forEach { companion ->
            helpersRateSum += companion.participationRate
            helpers.add(Helper(companion.id, companion.participationRate))
        }
        scanResultsRepository.initSecondaryScanInfo(
            SecondaryScanInfo(
                employeeId = employeeId ?: "",
                participationRate = 1F - helpersRateSum,
                helpers = helpers
            )
        )
    }
}

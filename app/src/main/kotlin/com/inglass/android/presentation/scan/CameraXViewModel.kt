package com.inglass.android.presentation.scan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.inglass.android.data.local.db.dao.CompanionsDao
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.domain.models.FullScannedItemModel
import com.inglass.android.domain.models.Helper
import com.inglass.android.domain.models.LoadingStatus.Queue
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.navigation.SCREENS.PREVIEW_PREFERENCE
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CameraXViewModel @Inject constructor(
    private val scanResultDao: ScanResultsDao,
    private val preferences: IPreferencesRepository,
    private val scanResultsRepository: IScanResultsRepository,
    private val companionsDao: CompanionsDao,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs = CameraXFragmentArgs.fromSavedStateHandle(savedStateHandle)

    val scanResSet: MutableSet<String> = mutableSetOf()
    var helpersRateSum = 0F
    var userRate = 1F
    val helpers = mutableListOf<Helper>()

    private val onScannedChannel = Channel<Unit>()
    val onScannedFlow = onScannedChannel.receiveAsFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            companionsDao.getCompanionsFullInfo().forEach { companion ->
                helpersRateSum += companion.participationRate
                helpers.add(Helper(companion.id, companion.participationRate))
            }

            userRate -= helpersRateSum
        }
    }

    fun checkBarcode(barcode: String) {
        if (barcode in scanResSet) return
        scanResSet.add(barcode)
        viewModelScope.launch(Dispatchers.IO) {
            if (scanResultDao.getItemById(barcode) == null) {
                onScannedChannel.send(Unit)
                saveBarcode(barcode)
                scanResultsRepository.emitScanResult(
                    FullScannedItemModel(
                        barcode = barcode,
                        loadingStatus = Queue,
                        employeeId = preferences.user?.id ?: return@launch,
                        operationId = navArgs.operationId,
                        dateTime = Calendar.getInstance().time, //TODO Перепроверить
                        participationRate = userRate,
                        helpers = helpers
                    )
                )
                if (navArgs.isSingleScan) {
                    navigateBack()
                }
            }
        }
    }

    private suspend fun saveBarcode(barcode: String) =
        scanResultDao.insertScanResult(
            ScanResult(
                barcode = barcode,
                operationId = navArgs.operationId,
                dateAndTime = Calendar.getInstance().time,
                loadingStatus = Queue
            )
        )

    fun navigateToPreferences() {
        navigateToScreen(PREVIEW_PREFERENCE)
    }
}

package com.inglass.android.presentation.scan

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.domain.models.LoadingStatus.Queue
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
    private val scanResultsRepository: IScanResultsRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs = CameraXFragmentArgs.fromSavedStateHandle(savedStateHandle)

    val scanResSet: MutableSet<String> = mutableSetOf()

    private val onScannedChannel = Channel<Unit>()
    val onScannedFlow = onScannedChannel.receiveAsFlow()

    fun checkBarcode(barcode: String) {
        if (barcode in scanResSet) return
        scanResSet.add(barcode)
        viewModelScope.launch(Dispatchers.IO) {
            if (scanResultsRepository.getItemById(barcode) == null) {
                onScannedChannel.send(Unit)
                val scanResult = ScanResult(
                    barcode = barcode,
                    operationId = navArgs.operationId,
                    dateAndTime = Calendar.getInstance().time,
                    loadingStatus = Queue,
                    error = null
                )
                scanResultsRepository.saveScanResult(scanResult)
                scanResultsRepository.emitScanResult(scanResult)
                if (navArgs.isSingleScan) {
                    navigateBack()
                }
            }
        }
    }

    fun navigateToPreferences() {
        navigateToScreen(PREVIEW_PREFERENCE)
    }
}

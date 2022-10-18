package com.inglass.android.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.data.local.db.entities.LoadingStatus
import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
import com.inglass.android.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class CameraXViewModel @Inject constructor(
    private val scanResultDao: ScanResultsDao,
    private val preferences: IPreferencesRepository,
    private val scanResultsRepository: IScanResultsRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val navArgs = CameraXFragmentArgs.fromSavedStateHandle(savedStateHandle)

    val isMultiScan = navArgs.isMultiScan

    val scanResSet: MutableSet<String> = mutableSetOf()

    fun checkBarcode(barcode: String) {
        if (barcode in scanResSet) return
        scanResSet.add(barcode)
        viewModelScope.launch(Dispatchers.IO) {
            if (scanResultDao.getItemById(barcode) == null) {
                saveBarcode(barcode)
                scanResultsRepository.emitScanResult(
                    ScanResult(
                        barcode,
                        navArgs.operationId,
                        Calendar.getInstance().time,
                        LoadingStatus.Queue
                    )
                )
            }
        }
    }

    private suspend fun saveBarcode(barcode: String) =
        scanResultDao.insertScanResult(
            ScanResult(
                barcode = barcode,
                operationId = navArgs.operationId,
                dateAndTime = Calendar.getInstance().time,
                loadingStatus = LoadingStatus.Queue
            )
        )
}

package com.inglass.android.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.data.local.db.entities.ScanResult
import com.inglass.android.domain.models.Helper
import com.inglass.android.domain.models.ScannedItemModel
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.scanning.MakeOperationUseCase
import com.inglass.android.domain.usecase.scanning.MakeOperationUseCase.Params
import com.inglass.android.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class CameraXViewModel @Inject constructor(
    private val scanResultDao: ScanResultsDao,
    private val makeOperationUseCase: MakeOperationUseCase,
    private val preferences: IPreferencesRepository,
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
                makeOperation()

            }
            Log.d("scanResult", barcode)

        }
    }

    private suspend fun saveBarcode(barcode: String) {
        val currentTime = Calendar.getInstance().time
        val scanResult = ScanResult(
            barcode = barcode,
            hasUploaded = false,
            operationId = navArgs.operationId,
            dateAndTime = currentTime,
        )
        scanResultDao.insertScanResult(scanResult)

//    HomeActivity.adapter.refreshView(db?.scanResultsDao()?.getLast2001()!!)
//    HomeActivity.adapter.notifyDataSetChanged()
    }

    private suspend fun makeOperation() {

        makeOperationUseCase.invoke(
            Params(
                "idididid",
                ScannedItemModel(
                    preferences.user?.id ?: return,
                    2,
                    323,
                    0.5F,
                    listOf(Helper(1, 0.4F)),
                    1
                )
            )
        )
    }

}

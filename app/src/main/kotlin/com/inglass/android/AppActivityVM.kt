package com.inglass.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.data.local.db.entities.LoadingStatus.InProgress
import com.inglass.android.data.local.db.entities.LoadingStatus.Loaded
import com.inglass.android.data.local.db.entities.LoadingStatus.NotLoaded
import com.inglass.android.domain.models.ScannedItemModel
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
import com.inglass.android.domain.usecase.scanning.MakeOperationUseCase
import com.inglass.android.domain.usecase.scanning.MakeOperationUseCase.Params
import com.inglass.android.utils.api.core.onFailure
import com.inglass.android.utils.api.core.onSuccess
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.provider.ConnectivityStatusProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class AppActivityVM @Inject constructor(
    private val connectivityStatusProvider: ConnectivityStatusProvider,
    private val makeOperationUseCase: MakeOperationUseCase,
    private val prefs: IPreferencesRepository,
    private val scanResultsRepository: IScanResultsRepository,
    private val scanResultDao: ScanResultsDao
) : BaseViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            scanResultsRepository.result.collect { scanResult ->
                scanResultDao.updateScanResult(scanResult.barcode, InProgress)
                makeOperationUseCase.invoke(
                    Params(
                        scanResult.barcode,
                        ScannedItemModel(
                            prefs.user?.id ?: return@collect,
                            scanResult.operationId,
                            scanResult.dateAndTime,
                            1F,
                            emptyList(),
                            1
                        )
                    )
                ).onSuccess {
                    scanResultDao.updateScanResult(scanResult.barcode, Loaded)
                }.onFailure {
                    scanResultDao.updateScanResult(scanResult.barcode, NotLoaded)
                }
            }
        }
    }

    val showMenu = MutableLiveData(false)

    val connectivityLiveData: LiveData<ConnectivityStatusProvider.ConnectivityStatus>
        get() = connectivityStatusProvider.connectionStatusLiveData

}

package com.inglass.android

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.data.local.db.entities.LoadingStatus.InProgress
import com.inglass.android.data.local.db.entities.LoadingStatus.Loaded
import com.inglass.android.data.local.db.entities.LoadingStatus.NotLoaded
import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.domain.models.ScannedItemModel
import com.inglass.android.domain.repository.interfaces.IPersonalInformationRepository
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
import com.inglass.android.domain.usecase.scanning.MakeOperationUseCase
import com.inglass.android.domain.usecase.scanning.MakeOperationUseCase.Params
import com.inglass.android.utils.api.core.onFailure
import com.inglass.android.utils.api.core.onSuccess
import com.inglass.android.utils.api.core.retry
import com.inglass.android.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AppActivityVM @Inject constructor(
    private val makeOperationUseCase: MakeOperationUseCase,
    private val prefs: IPreferencesRepository,
    private val scanResultsRepository: IScanResultsRepository,
    private val scanResultDao: ScanResultsDao,
    private val personalInformationRepository: IPersonalInformationRepository
) : BaseViewModel() {

    val showToast = MutableLiveData(false)
    val userInfo = MutableLiveData<PersonalInformationModel>()
    val host = MutableLiveData("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            scanResultsRepository.result.receiveAsFlow().collect { scanResult ->
                scanResultDao.updateScanResult(scanResult.barcode, InProgress)

                val isMakeOperation = retry(3) {
                    makeOperationUseCase.invoke(
                        Params(
                            scanResult.barcode,
                            ScannedItemModel(
                                employeeId = scanResult.employeeId,
                                operationId = scanResult.operationId,
                                dateTime = scanResult.dateTime,
                                participationRate = scanResult.participationRate,
                                helpers = scanResult.helpers
                            )
                        )
                    )
                }

                isMakeOperation.onSuccess {
                    scanResultDao.updateScanResult(scanResult.barcode, Loaded)
                    showToast.postValue(false)
                }
                isMakeOperation.onFailure {
                    scanResultDao.updateScanResult(scanResult.barcode, NotLoaded)
                    showToast.postValue(true)
                }
            }
        }
        observePersonalInformation()
        host.postValue(prefs.baseUrl)
    }

    private fun observePersonalInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            personalInformationRepository.result.filterNotNull().collect {
                userInfo.postValue(it)
            }
        }
    }
}

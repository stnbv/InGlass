package com.inglass.android

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inglass.android.domain.models.LoadingStatus.InProgress
import com.inglass.android.domain.models.LoadingStatus.Loaded
import com.inglass.android.domain.models.LoadingStatus.NotLoaded
import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.domain.models.ScannedItemModel
import com.inglass.android.domain.repository.interfaces.IAuthRepository
import com.inglass.android.domain.repository.interfaces.ICompanionsRepository
import com.inglass.android.domain.repository.interfaces.IPersonalInformationRepository
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.repository.interfaces.IScanResultsRepository
import com.inglass.android.domain.usecase.scanning.MakeOperationUseCase
import com.inglass.android.domain.usecase.scanning.MakeOperationUseCase.Params
import com.inglass.android.utils.api.core.ErrorCode.AuthorizationError
import com.inglass.android.utils.api.core.onFailure
import com.inglass.android.utils.api.core.onSuccess
import com.inglass.android.utils.api.core.retry
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.navigation.SCREENS.LOGIN
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
    private val personalInformationRepository: IPersonalInformationRepository,
    private val authRepository: IAuthRepository,
    private val companionsRepository: ICompanionsRepository
) : BaseViewModel() {

    val showToast = MutableLiveData(false)
    val userInfo = MutableLiveData<PersonalInformationModel>()
    val host = MutableLiveData("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            scanResultsRepository.result.receiveAsFlow().collect { scanResult ->
                scanResultsRepository.updateScanResult(scanResult.barcode, InProgress)

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
                    scanResultsRepository.updateScanResult(scanResult.barcode, Loaded)
                    showToast.postValue(false)
                }
                isMakeOperation.onFailure {
                    scanResultsRepository.updateScanResult(scanResult.barcode, NotLoaded)
                    showToast.postValue(true)
                    if (it.code == AuthorizationError) navigateToScreen(LOGIN)
                }
            }
        }
        host.postValue(prefs.baseUrl)
        observePersonalInformation()
        observeLogout()
    }

    fun clearPrefs() {
        viewModelScope.launch {
            prefs.clear()
        }
    }

    fun clearScanResultDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            scanResultsRepository.deleteAllItems()
        }
    }

    fun clearDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            scanResultsRepository.deleteAllItems()
            companionsRepository.deleteAllCompanions()
        }
    }

    private fun observePersonalInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            personalInformationRepository.result.filterNotNull().collect {
                userInfo.postValue(it)
            }
        }
    }

    private fun observeLogout() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.logOut.filterNotNull().collect {
                clearDatabase()
                prefs.clear()
                navigateToScreen(LOGIN)
            }
        }
    }
}

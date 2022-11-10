package com.inglass.android.presentation.main.desktop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.inglass.android.data.local.db.dao.CompanionsDao
import com.inglass.android.data.local.db.dao.EmployeeDao
import com.inglass.android.data.local.db.dao.OperationsDao
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.data.local.db.entities.Companions
import com.inglass.android.data.local.db.entities.Employee
import com.inglass.android.data.local.db.entities.Operation
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.WaitNetworkUseCase
import com.inglass.android.domain.usecase.companions.GetCompanionsUseCase
import com.inglass.android.domain.usecase.personal_information.GetPersonalInformationUseCase
import com.inglass.android.domain.usecase.reference_book.GetReferenceBookUseCase
import com.inglass.android.utils.adapter.ItemVM
import com.inglass.android.utils.api.core.ErrorCode.AuthorizationError
import com.inglass.android.utils.api.core.ErrorCode.InternalError
import com.inglass.android.utils.api.core.onFailure
import com.inglass.android.utils.api.core.onSuccess
import com.inglass.android.utils.base.paging.BasePagingViewModel
import com.inglass.android.utils.navigation.SCREENS.CAMERA
import com.inglass.android.utils.navigation.SCREENS.LOGIN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class DesktopVM @Inject constructor(
    private val getPersonalInformationUseCase: GetPersonalInformationUseCase,
    private val getReferenceBookUseCase: GetReferenceBookUseCase,
    private val preferencesRepository: IPreferencesRepository,
    private val scanResultDao: ScanResultsDao,
    private val operationsDao: OperationsDao,
    private val employeeDao: EmployeeDao,
    private val companionsDao: CompanionsDao,
    private val networkUseCase: WaitNetworkUseCase,
    private val getCompanionsUseCase: GetCompanionsUseCase
) : BasePagingViewModel() {

    val isScanButtonEnable = MutableLiveData(false)
    val operations = MutableLiveData(listOf("Нет доступных операций"))
    val selectedOperationsPosition = MutableLiveData(0)
    val isSingleScan = MutableLiveData(false)
    val helpersNames = MutableLiveData("")
    var userAvailableOperations: List<Operation?>? = null

    init {
        initViewModelWithRecycler()
        getReferenceBook()
        getUserInformation()
        getCompanions()
    }

    private fun getCompanions() {
        viewModelScope.launch(Dispatchers.IO) {
            getCompanionsUseCase().onSuccess { referenceBook ->
                companionsDao.insertCompanions(referenceBook.map {
                    Companions(it.id, it.participationRate)
                })
            }
                .onFailure {
                    Timber.e(it.message)
                }

            companionsDao.getCompanionsFullInfoFlow().collect {
                var helpersText = ""
                helpersNames.postValue("")
                it.forEach { helper ->
                    helpersText += "${helper.name} - ${helper.participationRate} /"
                    helpersNames.postValue("Помощники: $helpersText".trim().dropLast(1))
                }
            }
        }
    }

    fun setDataToItems() {
        viewModelScope.launch {
            val items = Pager(
                config = PagingConfig(
                    pageSize = 3,
                    enablePlaceholders = false,
                    initialLoadSize = 5
                ),
                pagingSourceFactory = { scanResultDao.getScanResultWithOperation() }
            ).flow.cachedIn(viewModelScope)

            items.collect {
                val scannedItems = it.map { scanResultFullInfo ->
                    ScannedItemVM(
                        ScannedItemData(
                            dateTime = scanResultFullInfo.dateAndTime,
                            operation = scanResultFullInfo.operationName,
                            barcode = scanResultFullInfo.barcode,
                            loadingStatus = scanResultFullInfo.loadingStatus
                        )
                    ) as ItemVM
                }
                pagingAdapter.submitData(scannedItems)
            }
        }
    }

    private fun getUserInformation() {
        viewModelScope.launch {
            getPersonalInformationUseCase()

            val userAvailableOperationsIds = preferencesRepository.user?.availableOperations
            val allOperations = operationsDao.getOperations()

            userAvailableOperations = userAvailableOperationsIds?.mapNotNull {
                allOperations.find { operation ->
                    operation.operationId == it
                }
            }

            val userOperations: List<String> = userAvailableOperations?.map {
                it!!.name
            } ?: listOf("Нет доступных операций")

            if (userOperations.isNotEmpty()) {
                operations.value = userOperations
            }
        }
    }

    private fun getReferenceBook() {
        if (System.currentTimeMillis() - preferencesRepository.lastReceivedData < 300000) return
        else {
            viewModelScope.launch(Dispatchers.IO) {
                do {
                    val result = getReferenceBookUseCase().onSuccess { referenceBook ->

                        operationsDao.insertOperations(referenceBook.operations.map { operation ->
                            Operation(operation.id, operation.name)
                        })

                        employeeDao.insertEmployee(referenceBook.employees.map { employee ->
                            Employee(employee.id, employee.name)
                        })
                    }
                        .onFailure {
                            when (it.code) {
                                AuthorizationError -> navigateToScreen(LOGIN)
                                InternalError -> networkUseCase.invoke()
                                else -> delay(3000)
                            }
                        }
                } while (result.isFailure && result.errorOrNull()?.code != AuthorizationError)
                preferencesRepository.lastReceivedData = System.currentTimeMillis()
            }
        }
    }

    fun openCameraScreen() {
        val operationTitle = operations.value?.get(selectedOperationsPosition.value ?: return)
        val operationId = userAvailableOperations?.find {
            it?.name == operationTitle
        }?.operationId

        navigateToScreen(CAMERA.apply {
            navDirections = DesktopFragmentDirections.toCamerax(
                operationId = operationId ?: return,
                isSingleScan = isSingleScan.value ?: return
            )
        })
    }

    fun changeScanCount(isChecked: Boolean) {
        isSingleScan.value = isChecked
    }
}

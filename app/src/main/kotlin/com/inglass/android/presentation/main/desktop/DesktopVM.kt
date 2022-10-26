package com.inglass.android.presentation.main.desktop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.inglass.android.data.local.db.dao.EmployeeDao
import com.inglass.android.data.local.db.dao.OperationsDao
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.data.local.db.dao.UserHelpersDao
import com.inglass.android.data.local.db.entities.Employee
import com.inglass.android.data.local.db.entities.Operation
import com.inglass.android.domain.models.EmployeeAndOperationModel
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.WaitNetworkUseCase
import com.inglass.android.domain.usecase.personal_information.GetPersonalInformationUseCase
import com.inglass.android.domain.usecase.reference_book.GetReferenceBookUseCase
import com.inglass.android.utils.adapter.ItemVM
import com.inglass.android.utils.api.core.ErrorCode.InternalError
import com.inglass.android.utils.api.core.onFailure
import com.inglass.android.utils.api.core.onSuccess
import com.inglass.android.utils.base.paging.BasePagingViewModel
import com.inglass.android.utils.navigation.SCREENS.CAMERA
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class DesktopVM @Inject constructor(
    private val getPersonalInformationUseCase: GetPersonalInformationUseCase,
    private val getReferenceBookUseCase: GetReferenceBookUseCase,
    private val preferencesRepository: IPreferencesRepository,
    private val scanResultDao: ScanResultsDao,
    private val operationsDao: OperationsDao,
    private val employeeDao: EmployeeDao,
    private val helpersDao: UserHelpersDao,
    private val referenceBookUseCase: GetReferenceBookUseCase,
    private val networkUseCase: WaitNetworkUseCase
) : BasePagingViewModel() {

    val isScanButtonEnable = MutableLiveData(false)
    val operations = MutableLiveData(listOf("Нет доступных операций"))
    val selectedOperationsPosition = MutableLiveData(0)
    val isMultiScan = MutableLiveData(false)
    val helpersNames = MutableLiveData("")
    var userAvailableOperations: List<EmployeeAndOperationModel?>? = null

    init {
        initViewModelWithRecycler()
        getReferenceBook()
        getUserInformation()
        getHelpers()
    }

    private fun getHelpers() {
        viewModelScope.launch(Dispatchers.IO) {
            helpersDao.getHelperFullInfoFlow().collect {
                helpersNames.postValue("")
                var helpersText = ""
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
                val scannedItems = it.map { scanResultWithOperation ->
                    ScannedItemVM(
                        ScannedItemData(
                            dateTime = scanResultWithOperation.scanResult.dateAndTime,
                            operation = scanResultWithOperation.operation?.name ?: "",
                            barcode = scanResultWithOperation.scanResult.barcode,
                            loadingStatus = scanResultWithOperation.scanResult.loadingStatus
                        )
                    ) as ItemVM
                }
                pagingAdapter.submitData(scannedItems)
            }
        }
    }

    private fun getUserInformation() {
        viewModelScope.launch {
            getPersonalInformationUseCase.invoke()

            val userAvailableOperationsIds = preferencesRepository.user?.availableOperations

            do {
                val result = getReferenceBookUseCase.invoke().onSuccess { allOperations ->
                    userAvailableOperations = userAvailableOperationsIds?.mapNotNull { userOperation ->
                        allOperations.operations.find {
                            userOperation == it.id
                        }
                    }

                    val userOperations: List<String> = userAvailableOperations?.map {
                        it!!.name
                    } ?: listOf("Нет доступных операций")

                    if (userOperations.isNotEmpty()) {
                        operations.value = userOperations
                    }
                }
                    .onFailure {
                        if (it.code == InternalError) {
                            networkUseCase.invoke()
                        } else {
                            delay(3000)
                        }
                    }
            } while (result.isFailure)
        }
    }

    private fun getReferenceBook() {
        if (System.currentTimeMillis() - preferencesRepository.lastReceivedData < 300000) return
        else {
            viewModelScope.launch {

                referenceBookUseCase().onSuccess { referenceBook ->

                    operationsDao.insertOperations(referenceBook.operations.map { operation ->
                        Operation(operation.id, operation.name)
                    })

                    employeeDao.insertEmployee(referenceBook.employees.map { employee ->
                        Employee(employee.id, employee.name)
                    })
                }
            }
            preferencesRepository.lastReceivedData = System.currentTimeMillis()
        }
    }

    fun openCameraScreen() {
        val operationTitle = operations.value?.get(selectedOperationsPosition.value ?: return)
        val operationId = userAvailableOperations?.find {
            it?.name == operationTitle
        }?.id

        navigateToScreen(CAMERA.apply {
            navDirections = DesktopFragmentDirections.toCamerax(
                operationId = operationId ?: return,
                isMultiScan = isMultiScan.value ?: return
            )
        })
    }

    fun clearDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            scanResultDao.deleteAllItems()
        }
    }
}

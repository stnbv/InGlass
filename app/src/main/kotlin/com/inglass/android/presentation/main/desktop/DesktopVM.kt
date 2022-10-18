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
import com.inglass.android.data.local.db.entities.Employee
import com.inglass.android.data.local.db.entities.Operation
import com.inglass.android.domain.models.EmployeeAndOperationModel
import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.personal_information.GetPersonalInformationUseCase
import com.inglass.android.domain.usecase.reference_book.GetReferenceBookUseCase
import com.inglass.android.utils.adapter.ItemVM
import com.inglass.android.utils.api.core.onSuccess
import com.inglass.android.utils.base.paging.BasePagingViewModel
import com.inglass.android.utils.navigation.SCREENS.CAMERA
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
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
    private val referenceBookUseCase: GetReferenceBookUseCase,
) : BasePagingViewModel() {

    val userInfo = MutableLiveData<PersonalInformationModel>()
    val isScanButtonEnable = MutableLiveData(false)
    val operations = MutableLiveData(mutableListOf("Выберите операцию"))
    val selectedOperations = MutableLiveData(0)
    val isMultiScan = MutableLiveData(false)

    init {
        initViewModelWithRecycler()
        getUserInformation()
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
                val a = it.map { scanResultWithOperation ->
                    ScannedItemVM(
                        ScannedItemData(
                            dateTime = scanResultWithOperation.scanResult.dateAndTime,
                            operation = scanResultWithOperation.operation?.name ?: "",
                            barcode = scanResultWithOperation.scanResult.barcode,
                            loadingStatus = scanResultWithOperation.scanResult.loadingStatus
                        )
                    ) as ItemVM
                }
                pagingAdapter.submitData(a)
            }
        }
    }

    private fun getUserInformation() {
        viewModelScope.launch {
            getPersonalInformationUseCase().collect {
                userInfo.value = it
            }

            val userAvailableOperationsIds = preferencesRepository.user?.availableOperations
            var userAvailableOperations: List<EmployeeAndOperationModel?>? = null

            getReferenceBookUseCase.invoke().onSuccess { allOperations ->
                userAvailableOperations = userAvailableOperationsIds?.mapNotNull { userOperation ->
                    allOperations.operations.find {
                        userOperation == it.id
                    }
                }

                userAvailableOperations?.map {
                    operations.value?.add(it!!.name)
                } ?: operations.value?.add("Нет доступных операция")
            }
        }
    }

    fun getReferenceBook() {
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
        navigateToScreen(CAMERA.apply {
            navDirections = DesktopFragmentDirections.toCamerax(
                operationId = selectedOperations.value ?: return,
                isMultiScan = isMultiScan.value ?: return
            )
        })
    }

    fun clearDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            scanResultDao.deleteAllItems()
            setData(emptyList())
        }
    }
}

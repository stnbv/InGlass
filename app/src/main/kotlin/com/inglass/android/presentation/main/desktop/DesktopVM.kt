package com.inglass.android.presentation.main.desktop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inglass.android.data.local.db.AppDatabase
import com.inglass.android.data.local.db.dao.ScanResultsDao
import com.inglass.android.domain.models.EmployeeAndOperationModel
import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.personal_information.GetPersonalInformationUseCase
import com.inglass.android.domain.usecase.reference_book.GetReferenceBookUseCase
import com.inglass.android.utils.api.core.onSuccess
import com.inglass.android.utils.base.BaseViewModel
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
    private val scanResultDao: ScanResultsDao
) : BaseViewModel() {

    val userInfo = MutableLiveData<PersonalInformationModel>()
    val isScanButtonEnable = MutableLiveData(false)
    val operations = MutableLiveData(mutableListOf("Выберите операцию"))
    val selectedOperations = MutableLiveData(0)
    val isMultiScan = MutableLiveData(false)

    init {
        initViewModelWithRecycler()
        getUserInformation()
    }

    fun setDataToItems(db: AppDatabase) {
        viewModelScope.launch(Dispatchers.IO) {
            val recs = db.scanResultsDao().getScannedItems()

            setData(recs.map {
                ScannedItemVM(
                    ScannedItemData(
                        dateTime = it.dateAndTime,
                        operation = it.operationId,
                        barcode = it.barcode
                    )
                )
            })
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
        }
    }
}

package com.inglass.android.presentation.main.desktop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.inglass.tasker.data.db.AppDatabase
import com.inglass.android.domain.models.EmployeeAndOperationModel
import com.inglass.android.domain.models.Helper
import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.domain.models.ScannedItemModel
import com.inglass.android.domain.repository.interfaces.IPreferencesRepository
import com.inglass.android.domain.usecase.personal_information.GetPersonalInformationUseCase
import com.inglass.android.domain.usecase.reference_book.GetReferenceBookUseCase
import com.inglass.android.domain.usecase.scanning.MakeOperationUseCase
import com.inglass.android.domain.usecase.scanning.MakeOperationUseCase.Params
import com.inglass.android.utils.api.core.onSuccess
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.navigation.DIALOGS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class DesktopVM @Inject constructor(
    private val getPersonalInformationUseCase: GetPersonalInformationUseCase,
    private val getReferenceBookUseCase: GetReferenceBookUseCase,
    private val makeOperationUseCase: MakeOperationUseCase,
    private val preferencesRepository: IPreferencesRepository
) : BaseViewModel() {

    val userInfo = MutableLiveData<PersonalInformationModel>()
    val isScanButtonEnable = MutableLiveData(false)
    val operations = MutableLiveData(mutableListOf("Выберите операцию"))

    init {
        initViewModelWithRecycler()
        getUserInformation()
    }

    fun setDataToItems(db: AppDatabase?) {
        val recs = db?.scanResultsDao()?.getScannedItems()!!

        setData(recs.map {
            ScannedItemVM(
                ScannedItemData(
                    dateTime = it.dateAndTime,
                    operation = it.operation,
                    barcode = it.barcode
                )
            )
        })
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


            makeOperationUseCase.invoke(
                Params(
                    "idididid",
                    ScannedItemModel(
                        1,
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

    fun openGeolocationDialog() {
        navigateToScreen(DIALOGS.ACCESS_TO_SETTINGS)
    }
}

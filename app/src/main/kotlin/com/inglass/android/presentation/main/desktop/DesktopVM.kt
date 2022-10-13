package com.inglass.android.presentation.main.desktop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.inglass.tasker.data.db.AppDatabase
import com.inglass.android.domain.models.PersonalInformationModel
import com.inglass.android.domain.usecase.personal_information.GetPersonalInformationUseCase
import com.inglass.android.presentation.main.scan2.ScannedItemData
import com.inglass.android.presentation.main.scan2.ScannedItemVM
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.navigation.DIALOGS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class DesktopVM @Inject constructor(
    private val getPersonalInformationUseCase: GetPersonalInformationUseCase
) : BaseViewModel() {

    val userInfo = MutableLiveData<PersonalInformationModel>()

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
        }
    }

    fun openGeolocationDialog() {
        navigateToScreen(DIALOGS.ACCESS_TO_SETTINGS)
    }
}

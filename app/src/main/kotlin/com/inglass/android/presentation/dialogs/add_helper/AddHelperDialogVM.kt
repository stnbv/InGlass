package com.inglass.android.presentation.dialogs.add_helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.inglass.android.data.local.db.dao.EmployeeDao
import com.inglass.android.data.local.db.dao.UserHelpersDao
import com.inglass.android.data.local.db.entities.Employee
import com.inglass.android.data.local.db.entities.UserHelpers
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.navigation.SCREENS.HELPERS
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class AddHelperDialogVM @Inject constructor(
    private val userHelpersDao: UserHelpersDao,
    private val employeeDao: EmployeeDao
) : BaseViewModel() {

    val selectedEmployeePosition = MutableLiveData(0)
    val participation = MutableLiveData("")
    var employeesItemsForSpinner = MutableLiveData(mutableListOf("Выберите помощника"))
    val isSaveButtonEnable = MutableLiveData(false)
    lateinit var employeeData: List<Employee>

    init {
        viewModelScope.launch(Dispatchers.IO) {
            employeeData = employeeDao.getEmployees()
            employeesItemsForSpinner.value?.addAll(employeeData.map {
                it.name
            })
        }
    }

    fun checkFields() {
        if (participation.value == "") return
        val isEmployeeSelected = selectedEmployeePosition.value != 0
        val isParticipationFilledCorrect =
            BigDecimal(participation.value).setScale(1, BigDecimal.ROUND_HALF_DOWN).remainder(
                BigDecimal(0.05).setScale(1, BigDecimal.ROUND_HALF_DOWN)
            ) == BigDecimal.ZERO.setScale(1, BigDecimal.ROUND_HALF_DOWN) && BigDecimal(participation.value).setScale(
                1,
                BigDecimal.ROUND_HALF_DOWN
            ) != BigDecimal.ZERO.setScale(1, BigDecimal.ROUND_HALF_DOWN)

        isSaveButtonEnable.value = isParticipationFilledCorrect && isEmployeeSelected
    }

    fun navigateToHelpers() { //TODO Подумать можно ли улучшить
        val employeeName =
            if (selectedEmployeePosition.value == null && employeesItemsForSpinner.value == null) return
            else employeesItemsForSpinner.value!![selectedEmployeePosition.value!!]

        val selectedEmployeeId = employeeData.find {
            it.name == employeeName
        }!!.id

        viewModelScope.launch(Dispatchers.IO) {
            userHelpersDao.insertHelper(UserHelpers(selectedEmployeeId, participation.value?.toFloat()!!))
        }

        navigateToScreen(HELPERS)
    }

    fun selectedChipText(text: String) {
        participation.postValue(text)
        checkFields()
    }
}

package com.inglass.android.presentation.helpers

//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.inglass.android.data.local.db.dao.EmployeeDao
//import com.inglass.android.data.local.db.dao.UserHelpersDao
//import com.inglass.android.data.local.db.entities.UserHelpers
//import com.inglass.android.presentation.dialogs.add_helper.EmployeeItemData
//import com.inglass.android.presentation.helpers.HelpersVM.SingleEvent.OnDeleteIconClick
//import com.inglass.android.presentation.helpers.HelpersVM.SingleEvent.OnParticipationRateClick
//import com.inglass.android.utils.adapter.ItemVM
//import com.inglass.android.utils.base.BaseViewModel
//import com.inglass.android.utils.navigation.DIALOGS.ADD_HELPER
//import com.inglass.android.utils.navigation.DIALOGS.ADD_PARTICIPATION
//import dagger.hilt.android.lifecycle.HiltViewModel
//import java.math.BigDecimal
//import java.math.BigDecimal.ZERO
//import javax.inject.Inject
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//@HiltViewModel
//class HelpersVM @Inject constructor(
//    private val userHelpersDao: UserHelpersDao,
//    private val employeeDao: EmployeeDao
//) : BaseViewModel() {
//
//    private var helpersItemData = mutableListOf<HelperItemData>()
//    private var employeeData = mutableListOf<EmployeeItemData>()
//    private var participationRateSummary = ZERO
//    var saveButtonEnabled = MutableLiveData(false)
//    val addHelperButtonEnabled = MutableLiveData(true)
//
//    init {
//        initViewModelWithRecycler()
//        initData()
//    }
//
//    fun checkEmployeeListSize() {
//        addHelperButtonEnabled.postValue(employeeData.size != 0)
//    }
//
//    //Добавляет долю участия
//    fun addParticipationRate(data: HelperItemData) {
//        viewModelScope.launch(Dispatchers.Main) {
//            val index = helpersItemData.indexOfFirst {
//                it.id == data.id
//            }
//            if (index == -1) return@launch
//
//            participationRateSummary -= helpersItemData[index].participation
//
//            helpersItemData[index] = helpersItemData[index].copy(participation = data.participation)
//
//            participationRateSummary += data.participation
//            checkParticipationRates()
//            setAdapterData()
//        }
//    }
//
//    //Проверка валидности доли участия
//    private fun checkParticipationRates() {
//        val isRateSumCorrect = participationRateSummary < BigDecimal.ONE
//
//        var isRateFilledCorrect = true
//
//        helpersItemData.forEachIndexed { index, helperItemData ->
//            val isRateNotNull = helperItemData.participation.compareTo(ZERO) != 0
//            isRateFilledCorrect = isRateFilledCorrect && isRateNotNull
//
//            helpersItemData[index] = helperItemData.copy(isParticipationValid = isRateSumCorrect && isRateNotNull)
//        }
//
//        saveButtonEnabled.value = isRateFilledCorrect && isRateSumCorrect
//    }
//
//    fun addHelper(data: EmployeeItemData) {
//        viewModelScope.launch(Dispatchers.Main) {
//            helpersItemData += HelperItemData(data.id, data.fullName, ZERO)
//            employeeData.remove(employeeData.find {
//                it.id == data.id
//            })
//            checkParticipationRates()
//            checkEmployeeListSize()
//            setAdapterData()
//        }
//    }
//
//    fun saveHelpers() {
//        viewModelScope.launch(Dispatchers.IO) {
//            userHelpersDao.deleteAllHelpers()
//            userHelpersDao.insertHelpers(helpersItemData.map {
//                UserHelpers(it.id, it.participation)
//            })
//        }
//        navigateBack()
//    }
//
//    private fun initData() {
//        viewModelScope.launch(Dispatchers.IO) {
//            employeeData = employeeDao.getEmployees().map {
//                EmployeeItemData(it.id, it.name)
//            }.toMutableList()
//
//            userHelpersDao.getHelperFullInfo().forEach { employee ->
//                helpersItemData.add(
//                    HelperItemData(
//                        employee.id,
//                        employee.name,
//                        employee.participationRate
//                    )
//                )
//            }
//
//            helpersItemData.forEach { helper ->
//                participationRateSummary += helper.participation
//                val element = employeeData.find { employee ->
//                    helper.id == employee.id
//                }
//                employeeData.remove(element)
//            }
//            checkEmployeeListSize()
//            withContext(Dispatchers.Main) {
//                setAdapterData()
//            }
//        }
//    }
//
//    private fun setAdapterData() {
//        val helpersFromDb = mutableListOf<ItemVM>(HelpersHeaderItemVM())
//
//        helpersFromDb += helpersItemData.map {
//            HelperItemVM(it)
//        }
//        setData(helpersFromDb)
//    }
//
//    private fun navigateToChangeParticipation(item: HelperItemData) {
//        navigateToScreen(ADD_PARTICIPATION.apply {
//            navDirections = HelpersFragmentDirections.toNavigationAddParticipationRate(
//                EmployeeItemData(item.id, item.fullName)
//            )
//        })
//    }
//
//    fun navigateToAddHelper() {
//        navigateToScreen(ADD_HELPER.apply {
//            navDirections = HelpersFragmentDirections.toNavigationAddHelpersBs(
//                employeeData.toTypedArray()
//            )
//        })
//    }
//
//    private fun removeHelper(item: HelperItemData) {
//        helpersItemData.remove(item)
//        participationRateSummary -= item.participation
//        employeeData.add(EmployeeItemData(item.id, item.fullName))
//        checkParticipationRates()
//        checkEmployeeListSize()
//        setAdapterData()
//    }
//
//    fun removeAllHelpers() {
//        helpersItemData.forEach {
//            employeeData.add(EmployeeItemData(it.id, it.fullName))
//        }
//        participationRateSummary = ZERO
//        helpersItemData.clear()
//        checkParticipationRates()
//        checkEmployeeListSize()
//        setAdapterData()
//    }
//
//    override fun observeEvents(event: Any) {
//        when (event) {
//            is OnDeleteIconClick -> removeHelper(event.item)
//            is OnParticipationRateClick -> navigateToChangeParticipation(event.item)
//        }
//    }
//
//    sealed interface SingleEvent {
//        data class OnDeleteIconClick(val item: HelperItemData) : SingleEvent
//        data class OnParticipationRateClick(val item: HelperItemData) : SingleEvent
//    }
//}

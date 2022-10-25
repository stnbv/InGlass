package com.inglass.android.presentation.dialogs.add_helper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.inglass.android.presentation.dialogs.add_helper.AddHelperBottomSheetVM.SingleEvent.OnItemClick
import com.inglass.android.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddHelperBottomSheetVM @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    var selectedEmployee = MutableLiveData<EmployeeItemData>()
    val navArgs = AddHelperBottomSheetFragmentArgs.fromSavedStateHandle(savedStateHandle)

    init {
        initViewModelWithRecycler()
        setEmployeesNames()
    }

    fun setEmployeesNames() {
        setData(navArgs.employees.map {
            EmployeeItemVM(it)
        })
    }

    override fun observeEvents(event: Any) {
        when (event) {
            is OnItemClick -> selectedEmployee.value = event.item
        }
    }

    sealed interface SingleEvent {
        data class OnItemClick(val item: EmployeeItemData) : SingleEvent
    }
}

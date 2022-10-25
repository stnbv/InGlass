package com.inglass.android.presentation.dialogs.add_helper

import android.os.Parcelable
import com.inglass.android.R
import com.inglass.android.databinding.ItemHelperBinding
import com.inglass.android.presentation.dialogs.add_helper.AddHelperBottomSheetVM.SingleEvent
import com.inglass.android.utils.adapter.CommonItemVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

data class EmployeeItemVM(
    val data: EmployeeItemData
) : CommonItemVM<ItemHelperBinding>(R.layout.item_employee) {
    var fullName = data.fullName

    fun onItemClick() {
        CoroutineScope(Dispatchers.IO).launch {
            emitEvent(SingleEvent.OnItemClick(data))
        }
    }
}

@Parcelize
data class EmployeeItemData(
    val id: String,
    val fullName: String
) : Parcelable

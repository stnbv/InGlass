package com.inglass.android.presentation.helpers

import com.inglass.android.R
import com.inglass.android.databinding.ItemHelperBinding
import com.inglass.android.utils.adapter.CommonItemVM

data class HelperItemVM(
    val data: HelperItemData
) : CommonItemVM<ItemHelperBinding>(R.layout.item_helper) {
    var fullName = data.fullName
    var participation = data.participation
}

data class HelperItemData(
    val fullName: String,
    val participation: String
)

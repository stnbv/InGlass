package com.inglass.android.presentation.main.desktop

import com.inglass.android.R
import com.inglass.android.databinding.ItemScannedBinding
import com.inglass.android.utils.adapter.CommonItemVM
import com.inglass.android.utils.helpers.DateFormatHelper
import java.util.*

class ScannedItemVM(
    val data: ScannedItemData
) : CommonItemVM<ItemScannedBinding>(R.layout.item_scanned) {
    var dateTime = DateFormatHelper.FullDateTimeStamp.format(data.dateTime)
    var operation = data.operation.toString()
    var barcode = data.barcode + " в количестве 1 шт."
}

data class ScannedItemData(
    val dateTime: Date,
    val operation: Int,
    val barcode: String?
)

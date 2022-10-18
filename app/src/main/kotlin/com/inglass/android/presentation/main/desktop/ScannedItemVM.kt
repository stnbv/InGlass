package com.inglass.android.presentation.main.desktop

import com.inglass.android.R
import com.inglass.android.data.local.db.entities.LoadingStatus
import com.inglass.android.data.local.db.entities.LoadingStatus.InProgress
import com.inglass.android.data.local.db.entities.LoadingStatus.Loaded
import com.inglass.android.data.local.db.entities.LoadingStatus.NotLoaded
import com.inglass.android.data.local.db.entities.LoadingStatus.Queue
import com.inglass.android.databinding.ItemScannedBinding
import com.inglass.android.utils.adapter.CommonItemVM
import com.inglass.android.utils.helpers.DateFormatHelper
import java.util.*

class ScannedItemVM(
    val data: ScannedItemData
) : CommonItemVM<ItemScannedBinding>(R.layout.item_scanned) {
    var dateTime = DateFormatHelper.FullDateTimeStamp.format(data.dateTime)
    var operation = data.operation
    var barcode = data.barcode + " в количестве 1 шт."

    val loadStatusImage = when (data.loadingStatus) {
        NotLoaded -> R.drawable.ic_attention_error
        Queue -> R.drawable.ic_cloud_upload
        InProgress -> R.drawable.ic_arrow_down //TODO Заменить иконку
        Loaded -> R.drawable.ic_cloud_done
    }
}

data class ScannedItemData(
    val dateTime: Date,
    val operation: String,
    val barcode: String?,
    val loadingStatus: LoadingStatus = Queue
)

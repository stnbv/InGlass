package com.inglass.android.presentation.main.desktop

import com.inglass.android.R
import com.inglass.android.databinding.ItemScannedBinding
import com.inglass.android.domain.models.LoadingStatus
import com.inglass.android.domain.models.LoadingStatus.InProgress
import com.inglass.android.domain.models.LoadingStatus.Loaded
import com.inglass.android.domain.models.LoadingStatus.NotLoaded
import com.inglass.android.domain.models.LoadingStatus.Queue
import com.inglass.android.utils.adapter.CommonItemVM
import com.inglass.android.utils.helpers.DateFormatHelper
import java.util.*

class ScannedItemVM(
    val data: ScannedItemData
) : CommonItemVM<ItemScannedBinding>(R.layout.item_scanned) {
    var dateTime = DateFormatHelper.FullDateTimeStamp.format(data.dateTime)
    var operation = data.operation
    var barcode = data.barcode + " в количестве 1 шт." //TODO Заменить на строковый ресурс с парметром

    val loadStatusImage = when (data.loadingStatus) {
        NotLoaded -> R.drawable.ic_error
        Queue -> R.drawable.ic_upload
        InProgress -> R.drawable.ic_load
        Loaded -> R.drawable.ic_done_in_circle
    }
}

data class ScannedItemData(
    val dateTime: Date,
    val operation: String,
    val barcode: String?,
    val loadingStatus: LoadingStatus = Queue
)

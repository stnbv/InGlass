package com.inglass.android.presentation.main.desktop

import app.inglass.tasker.data.db.AppDatabase
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.navigation.DIALOGS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DesktopVM @Inject constructor() : BaseViewModel() {

     init {
        initViewModelWithRecycler()
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

    fun openGeolocationDialog() {
        navigateToScreen(DIALOGS.ACCESS_TO_SETTINGS)
    }
}

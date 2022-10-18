package com.inglass.android.presentation.helpers

import com.inglass.android.utils.adapter.ItemVM
import com.inglass.android.utils.base.BaseViewModel
import com.inglass.android.utils.navigation.DIALOGS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HelpersVM @Inject constructor() : BaseViewModel() {

    init {
        initViewModelWithRecycler()
    }

    fun setDataToItems() {

        val source = mutableListOf<ItemVM>()

        source.addAll(
            mutableListOf(
                HelperItemVM(HelperItemData("Иванов Иван Иванович", "0.6")),
                HelperItemVM(HelperItemData("Иванов Иван Иванович", "0.6")),
                HelperItemVM(HelperItemData("Иванов Иван Иванович", "0.6")),
                HelperItemVM(HelperItemData("Иванов Иван Иванович", "0.6")),
                HelperItemVM(HelperItemData("Иванов Иван Иванович", "0.6"))
            )
        )

        source.add(0, HelpersHeaderItemVM())
        setData(source)
    }

    fun openGeolocationDialog() {
        navigateToScreen(DIALOGS.ACCESS_TO_SETTINGS)
    }
}

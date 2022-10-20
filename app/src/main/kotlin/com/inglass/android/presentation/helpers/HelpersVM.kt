package com.inglass.android.presentation.helpers

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.TerminalSeparatorType.SOURCE_COMPLETE
import androidx.paging.cachedIn
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.inglass.android.data.local.db.dao.UserHelpersDao
import com.inglass.android.utils.adapter.ItemVM
import com.inglass.android.utils.base.paging.BasePagingViewModel
import com.inglass.android.utils.navigation.DIALOGS.ADD_HELPER
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@HiltViewModel
class HelpersVM @Inject constructor(
    private val userHelpersDao: UserHelpersDao
) : BasePagingViewModel() {

    init {
        initViewModelWithRecycler()
    }

    fun setDataToItems() {
        viewModelScope.launch {
            val items = Pager(
                config = PagingConfig(
                    pageSize = 3,
                    enablePlaceholders = false,
                    initialLoadSize = 5
                ),
                pagingSourceFactory = { userHelpersDao.getAllHelpers() }
            ).flow.cachedIn(viewModelScope)

            items.collect {
                val userHelpers = it.map { helpersFullData ->
                    HelperItemVM(
                        HelperItemData(
                            helpersFullData.employee.name,
                            helpersFullData.userHelpers.participationRate.toString()
                        )
                    ) { removeHelper(helpersFullData.userHelpers.helperId) } as ItemVM

                }
                    .insertHeaderItem(item = HelpersHeaderItemVM(), terminalSeparatorType = SOURCE_COMPLETE)

                pagingAdapter.submitData(userHelpers)
            }
        }
    }

    fun navigateToAddHelper() {
        navigateToScreen(ADD_HELPER)
    }

    private fun removeHelper(helperId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            userHelpersDao.deleteHelper(helperId)
        }
    }

    private fun removeAllHelpers() {
        viewModelScope.launch(Dispatchers.IO) {
            userHelpersDao.deleteAllHelpers()
        }
    }
}

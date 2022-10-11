package com.inglass.android.utils.adapter.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

fun PagingAdapter.checkLoadPagingData(liveData: MutableLiveData<Boolean>) {
    this.addLoadStateListener { loadState ->
        loadState.decideOnState(
            this,
            showLoad = { visible -> liveData.value = visible },
            showEmptyState = { visible -> liveData.value = visible })
    }
}

private fun CombinedLoadStates.decideOnState(
    pagingAdapter: PagingAdapter,
    showLoad: (Boolean) -> Unit,
    showEmptyState: (Boolean) -> Unit,
) {
    showLoad(refresh is LoadState.Loading && pagingAdapter.itemCount > 0)
    showEmptyState(
        source.append is LoadState.NotLoading
                && source.append.endOfPaginationReached
                && pagingAdapter.itemCount == 0
    )
}

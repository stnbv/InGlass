package com.inglass.android.utils.base.paging

import androidx.databinding.BindingAdapter
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.inglass.android.utils.adapter.paging.PagingAdapter
import com.inglass.android.utils.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BasePagingViewModel : BaseViewModel() {

    val pagingAdapter = PagingAdapter()

    init {
        viewModelScope.launch {
            pagingAdapter.observableEvents.receiveAsFlow().collect {
                observeEvents(it)
            }
        }
    }
}

@BindingAdapter("setPagingAdapter")
fun setPagingAdapter(view: RecyclerView, adapter: PagingAdapter) {
    if (view.adapter == null) {
        view.adapter = adapter
    }
}

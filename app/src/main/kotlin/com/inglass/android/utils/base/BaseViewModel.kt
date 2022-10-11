package com.inglass.android.utils.base

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.inglass.android.utils.SingleLiveEvent
import com.inglass.android.utils.adapter.CommonAdapter
import com.inglass.android.utils.adapter.ItemVM
import com.inglass.android.utils.analitics.AnalyticsManager
import com.inglass.android.utils.api.core.Answer.Failure
import com.inglass.android.utils.navigation.DIALOGS
import com.inglass.android.utils.navigation.SCREENS
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    var adapter: CommonAdapter? = null

    @Inject
    lateinit var analitycs: AnalyticsManager

    private val error = SingleLiveEvent<Failure>()
    val errorLiveData: LiveData<Failure>
        get() = error

    private val backChannel = Channel<Unit>()
    val backFlow = backChannel.receiveAsFlow()

    private val screenChannel = Channel<SCREENS>(Channel.BUFFERED)
    val screenFlow = screenChannel.receiveAsFlow()

    private val dialogChannel = Channel<DIALOGS>(Channel.BUFFERED)
    val screenDialogFlow = dialogChannel.receiveAsFlow()

    open fun initViewModelWithRecycler() {
        adapter = CommonAdapter()
        viewModelScope.launch {
            adapter?.observableEvents?.receiveAsFlow()?.collect {
                observeEvents(it)
            }
        }
    }

    open fun observeEvents(event: Any) = Unit

    protected fun setError(failure: Failure) {
        error.value = failure
    }

    fun setData(data: List<ItemVM>) {
        adapter?.submitList(data)
    }

    fun navigateToScreen(screen: SCREENS) {
        viewModelScope.launch(Dispatchers.Main) {
            screenChannel.send(screen)
        }
    }

    fun navigateToScreen(dialog: DIALOGS) {
        viewModelScope.launch(Dispatchers.Main) {
            dialogChannel.send(dialog)
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            backChannel.send(Unit)
        }
    }
}

@BindingAdapter("setOnlyAdapter")
fun setOnlyAdapter(view: RecyclerView, adapter: CommonAdapter) {
    if (view.adapter == null) {
        view.adapter = adapter
    }
}

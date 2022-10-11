package com.inglass.android.utils.adapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlinx.coroutines.channels.Channel
import timber.log.Timber

open class CommonItemVM<BINDING : ViewDataBinding>(@LayoutRes private val layoutId: Int) : ItemVM {

    var binding: BINDING? = null

    override var observableEvents = Channel<Any>()

    open suspend fun emitEvent(value: Any) {
        observableEvents.send(value)
    }

    override fun getLayout() = layoutId

    override fun getBinding(view: View): BINDING? {
        return try {
            binding = DataBindingUtil.bind(view)
            binding?.let { onBindingCreated(it) }
            binding
        } catch (e: BindingException) {
            Timber.tag("CommonItemVM").d("Cannot inflate class")
            null
        }
    }

    open fun onBindingCreated(binding: BINDING) = Unit

    override fun areItemsTheSame(oldItem: ItemVM) = this == oldItem
    override fun areContentsTheSame(oldItem: ItemVM) = this == oldItem

    class BindingException(message: String) : Exception(message)
}

package com.inglass.android.utils.adapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlinx.coroutines.channels.Channel

interface ItemVM {
    var observableEvents: Channel<Any>

    @LayoutRes
    fun getLayout(): Int

    fun getBinding(view: View) = DataBindingUtil.getBinding<ViewDataBinding>(view)

    fun areItemsTheSame(oldItem: ItemVM): Boolean

    fun areContentsTheSame(oldItem: ItemVM): Boolean

    fun getChangePayload(oldItem: ItemVM): Any? = null
}

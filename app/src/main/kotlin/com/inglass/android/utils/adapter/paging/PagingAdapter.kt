package com.inglass.android.utils.adapter.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.inglass.android.utils.adapter.ItemVM
import com.inglass.android.utils.adapter.ViewHolder
import kotlinx.coroutines.channels.Channel

open class PagingAdapter : PagingDataAdapter<ItemVM, ViewHolder>(DiffCallback) {

    open var observableEvents = Channel<Any>()

    private object DiffCallback : DiffUtil.ItemCallback<ItemVM>() {
        override fun areItemsTheSame(oldItem: ItemVM, newItem: ItemVM) = oldItem == newItem
        override fun areContentsTheSame(oldItem: ItemVM, newItem: ItemVM) = newItem.areContentsTheSame(oldItem)
        override fun getChangePayload(oldItem: ItemVM, newItem: ItemVM) = newItem.getChangePayload(oldItem)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.getLayout() ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)?.apply {
            observableEvents = this@PagingAdapter.observableEvents
        } ?: return
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(viewType, parent, false))
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams && holder.layoutPosition == 0) {
            layoutParams.isFullSpan = true
        }
    }
}

package com.inglass.android.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kotlinx.coroutines.channels.Channel

open class CommonAdapter : ListAdapter<ItemVM, ViewHolder>(DiffCallback) {

    open var observableEvents = Channel<Any>()

    private object DiffCallback : DiffUtil.ItemCallback<ItemVM>() {
        override fun areItemsTheSame(oldItem: ItemVM, newItem: ItemVM) = newItem.areItemsTheSame(oldItem)
        override fun areContentsTheSame(oldItem: ItemVM, newItem: ItemVM) = newItem.areContentsTheSame(oldItem)
        override fun getChangePayload(oldItem: ItemVM, newItem: ItemVM) = newItem.getChangePayload(oldItem)
    }

    fun getItemOrNull(pos: Int): ItemVM? {
        if (pos < 0 || pos >= itemCount) {
            return null
        }

        return getItem(pos)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getLayout()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position).apply {
            observableEvents = this@CommonAdapter.observableEvents
        }
        holder.bind(item)
    }
}

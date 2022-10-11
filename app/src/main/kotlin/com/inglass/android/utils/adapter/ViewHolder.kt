package com.inglass.android.utils.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.inglass.android.BR

class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    fun bind(vm: ItemVM) {
        vm.getBinding(itemView)?.setVariable(BR.vm, vm)
    }
}

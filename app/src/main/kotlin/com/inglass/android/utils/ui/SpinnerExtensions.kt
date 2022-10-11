package com.inglass.android.utils.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat.getFont
import com.inglass.android.R

fun setupHintTextViewSpinner(
    spinner: Spinner,
    hintColor: Int,
    itemTextColor: Int,
    data: List<String>,
    @LayoutRes layoutId: Int = R.layout.layout_custom_spinner,
    @LayoutRes dropdownViewId: Int = R.layout.spinner_item_main
) {
    val adapter = getHintSpinnerArrayAdapter(spinner.context, data, layoutId, hintColor, itemTextColor)
    adapter.setDropDownViewResource(dropdownViewId)
    spinner.adapter = adapter
    spinner.onItemSelectedListener = getHintSpinnerSelectedListener(hintColor)
}

fun getHintSpinnerSelectedListener(hintColor: Int) = BindingSpinnerListener(hintColor)

fun getHintSpinnerArrayAdapter(
    ctx: Context,
    data: List<String>,
    @LayoutRes layoutId: Int,
    hintColor: Int,
    textColor: Int
) = object : ArrayAdapter<String>(ctx, layoutId, data) {
    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val textView = super.getDropDownView(position, convertView, parent) as TextView
        val color = if (position == 0) hintColor else textColor
        textView.setTextColor(color)
        return textView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return (super.getView(position, convertView, parent) as TextView).apply {
            if (position == 0) setTextColor(hintColor)
        }
    }
}

class BindingSpinnerListener(private val hintColor: Int) : AdapterView.OnItemSelectedListener {
    var bindingCallback: (() -> Unit)? = null
    override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (view !is TextView) return
        view.typeface = if (position > 0) {
            adapterView?.setSelection(position)
            getFont(view.context, R.font.roboto_medium)
        } else {
            (view as? TextView)?.setTextColor(hintColor)
            getFont(view.context, R.font.roboto_regular)
        }
        bindingCallback?.invoke()
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) = Unit
}

// Гендеры должны идти по-порядку как в strings (важно!!!)
enum class Gender(val gender: String) {
    Default(""), Male("male"), Female("female")
}

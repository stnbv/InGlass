package com.inglass.android.utils.binding

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.inglass.android.R
import com.inglass.android.utils.ErrorWrapper

private const val PROGRESS_BAR_WIDTH = 5f
private const val PROGRESS_BAR_RADIUS = 30f

@BindingAdapter("app:visibility")
fun setVisibility(v: View, isVisible: Boolean?) {
    if (isVisible == null) return
    v.isVisible = isVisible
}

@BindingAdapter("app:drawableId")
fun setImage(view: ImageView, @DrawableRes resId: Int) {
    if (resId == 0) return
    view.setImageResource(resId)
}

@BindingAdapter("app:stringId")
fun setString(view: TextView, @StringRes resId: Int) {
    if (resId == 0) return
    view.text = view.context.getString(resId)
}

@BindingAdapter("app:textColorId")
fun setTextColor(view: TextView, @ColorRes resId: Int) {
    if (resId == 0) return
    view.setTextColor(view.context.getColor(resId))
}

@BindingAdapter(value = ["bindErrorWrapperText", "errorDisplayText", "wrapperHintColor"], requireAll = false)
fun bindErrorWrapper(view: View, error: ErrorWrapper, errorDisplayText: Boolean, wrapperHintColor: Int?) {
    if (errorDisplayText) {
        (view as TextView).text = error.getText(view.context)
    } else {
        val (drawable, color) = if (error.isThereError) {
            R.drawable.bg_edit_text_error to R.color.red
        } else {
            R.drawable.bg_edit_text to R.color.dark_gray
        }

        if (view is Spinner) {
            (view.selectedView as? TextView)?.let {
                view.background = if (error.isThereError) {
                    ContextCompat.getDrawable(view.context, drawable)
                } else {
                    ContextCompat.getDrawable(view.context, drawable)
                }
                it.setTextColor(ContextCompat.getColor(view.context, color))
            }
        } else {
            view.background = ContextCompat.getDrawable(view.context, drawable)
            (view as TextInputEditText).let {
                view.setTextColor(ContextCompat.getColor(view.context, color))
                val hColor = wrapperHintColor ?: ContextCompat.getColor(
                    view.context,
                    if (error.isThereError) color else R.color.light_grey
                )
                view.setHintTextColor(hColor)
            }
        }
    }
}

@BindingAdapter("unfocusedHint")
fun setHintWhenUnfocused(view: EditText, hint: String?) {
    hint?.let {
        if (!view.hasFocus()) view.hint = hint
        view.setOnFocusChangeListener { _, hasFocus ->
            view.hint = if (hasFocus) null else hint
        }
    }
}

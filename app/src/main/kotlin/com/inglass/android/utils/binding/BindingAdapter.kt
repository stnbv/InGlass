package com.inglass.android.utils.binding

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.inglass.android.R
import com.inglass.android.utils.ErrorWrapper
import com.inglass.android.utils.ui.BindingSpinnerListener
import com.inglass.android.utils.ui.dp
import java.math.BigDecimal

private const val PROGRESS_BAR_WIDTH = 5f
private const val PROGRESS_BAR_RADIUS = 30f

@BindingAdapter("app:visibility")
fun setVisibility(v: View, isVisible: Boolean?) {
    if (isVisible == null) return
    v.isVisible = isVisible
}

@BindingAdapter("app:invisibility")
fun setInvisibility(v: View, isInvisible: Boolean) {
    v.isInvisible = isInvisible
}

@BindingAdapter("app:genderAttrChanged")
fun setGenderListener(view: Spinner, attrChange: InverseBindingListener?) {
    view.onItemSelectedListener?.let { listener ->
        if (listener is BindingSpinnerListener && listener.bindingCallback == null) {
            listener.bindingCallback = { attrChange?.onChange() }
        }
    }
}

@BindingAdapter("app:stringUrl")
fun loadImage(view: ImageView, url: String?) {
    if (url.isNullOrBlank()) return
    val circularProgressDrawable = CircularProgressDrawable(view.context).apply {
        colorFilter = PorterDuffColorFilter(ContextCompat.getColor(view.context, R.color.pink), PorterDuff.Mode.SRC_IN)
        strokeWidth = PROGRESS_BAR_WIDTH
        centerRadius = PROGRESS_BAR_RADIUS
        start()
    }

    Glide
        .with(view)
        .load(url)
        .placeholder(circularProgressDrawable)
        .fitCenter()
        .into(view)
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

@BindingAdapter(value = ["app:topMargin", "app:bottomMargin", "app:startMargin", "app:endMargin"], requireAll = false)
fun setMargin(v: View, top: Int?, bottom: Int?, start: Int?, end: Int?) {
    v.layoutParams = (v.layoutParams as ViewGroup.MarginLayoutParams).apply {
        topMargin = top?.dp() ?: topMargin
        bottomMargin = bottom?.dp() ?: bottomMargin
        marginStart = start?.dp() ?: marginStart
        marginEnd = end?.dp() ?: marginEnd
    }
}

@BindingAdapter("app:recyclerSpaceElement")
fun setRecyclerSpace(recyclerView: RecyclerView, space: Int) {
    val itemDecoration = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            parent.adapter?.itemCount?.let {
                if (parent.getChildAdapterPosition(view) != it - 1) outRect.bottom = space.dp()
            }
        }
    }

    recyclerView.addItemDecoration(itemDecoration)
}

@BindingAdapter(value = ["bindErrorWrapperText", "errorDisplayText", "wrapperHintColor"], requireAll = false)
fun bindErrorWrapper(view: View, error: ErrorWrapper, errorDisplayText: Boolean, wrapperHintColor: Int?) {
    if (errorDisplayText) {
        (view as TextView).text = error.getText(view.context)
    } else {
        val (drawable, color) = if (error.isThereError) {
            R.drawable.bg_edit_text_error to R.color.pink
        } else {
            R.drawable.bg_edit_text to R.color.prompt_black
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
                    if (error.isThereError) color else R.color.normal_gray
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

@BindingAdapter("app:viewVisibility")
fun viewVisibility(view: View, price: BigDecimal) {
    view.isVisible = price.toInt() != 0
}

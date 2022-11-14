package com.inglass.android.utils.ui

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.text.Annotation
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.inglass.android.R
import com.inglass.android.databinding.LayoutCustomToastBinding

private const val DEFAULT_DELAY = 3000
private const val VIEW_INDEX = 0

@ColorInt
fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

@ColorInt
fun Resources.color(@ColorRes colorId: Int) = ResourcesCompat.getColor(this, colorId, null)

fun dpToPx(dp: Int) = (dp * Resources.getSystem().displayMetrics.density).toInt()

fun Int.dp() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline fun View.doOnClick(crossinline action: () -> Unit) {
    this.setOnClickListener { action() }
}

fun ViewGroup.makeToast(
    toastGravity: Int = Gravity.TOP,
    @ColorInt backgroundRes: Int,
    message: String = "",
    @DrawableRes imageStatusRes: Int? = null,
    duration: Int = DEFAULT_DELAY
) {
    val inflater = LayoutInflater.from(this.context)

    val snackBar = Snackbar.make(this, message, duration)

    val layoutParams = FrameLayout.LayoutParams(snackBar.view.layoutParams)
    layoutParams.gravity = toastGravity
    with(context.resources) {
        layoutParams.setMargins(
            getDimensionPixelSize(R.dimen.toast_margin_horizontal),
            getDimensionPixelSize(R.dimen.toast_margin_top),
            getDimensionPixelSize(R.dimen.toast_margin_horizontal),
            getDimensionPixelSize(R.dimen.toast_margin_bot)
        )
    }
    snackBar.view.layoutParams = layoutParams

    snackBar.view.setBackgroundColor(Color.TRANSPARENT)
    val snackBarLayout = snackBar.view as SnackbarLayout
    (snackBarLayout.findViewById(R.id.snackbar_text) as TextView).visibility = View.INVISIBLE

    val binding = LayoutCustomToastBinding.inflate(inflater)

    with(binding) {
        customToast.backgroundTintList = ColorStateList.valueOf(backgroundRes)
        messageTv.text = message
        if (imageStatusRes != null) statusIv.setImageResource(imageStatusRes)
        snackBarLayout.addView(root, VIEW_INDEX)
    }
    snackBar.show()
}


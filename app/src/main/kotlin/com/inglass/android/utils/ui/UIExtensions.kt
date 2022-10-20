package com.inglass.android.utils.ui

import android.content.Context
import android.content.res.Resources
import android.text.Annotation
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.color.MaterialColors
import com.inglass.android.R

private const val SPAN_KEY_LINK = "link"

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

fun Context.setSpans(
    initSpannable: SpannableString,
    resSpannable: SpannableString,
    listener: (value: String) -> Unit,
): SpannableString {
    val color = MaterialColors.getColor(this, R.attr.colorAccent, 0)

    initSpannable.getSpans(0, initSpannable.length, Annotation::class.java)
        .filter { it.key == SPAN_KEY_LINK }
        .forEach {
            resSpannable.setSpan(
                ForegroundColorSpan(color),
                initSpannable.getSpanStart(it),
                initSpannable.getSpanEnd(it),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            resSpannable.setSpan(
                object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        listener(it.value)
                    }

                    override fun updateDrawState(drawState: TextPaint) {
                        drawState.isUnderlineText = false
                    }
                },
                initSpannable.getSpanStart(it),
                initSpannable.getSpanEnd(it),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

    return resSpannable
}

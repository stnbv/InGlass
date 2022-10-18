package com.inglass.android.utils.ui

import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged

fun TextView.setDrawableStart(@DrawableRes drawableId: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0)
}

fun TextView.setDrawableStart(drawable: Drawable) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
}

fun TextView.setDrawableEnd(@DrawableRes id: Int) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, id, 0)
}

fun TextView.setColor(@ColorRes colorId: Int) {
    setTextColor(resources.color(colorId))
}

inline fun TextView.doOnTextChanged(crossinline action: (text: String) -> Unit) {
    doOnTextChanged { text, _, _, _ ->
        action(text?.toString() ?: "")
    }
}

fun TextView.setTextWithLinksAndArg(
    @StringRes res: Int,
    arg: String,
    listener: (value: String) -> Unit,
) {
    movementMethod = LinkMovementMethod.getInstance()
    val textWithArgs = context.getString(res, arg)
    val styledSpannable = SpannableString(context.getText(res))

    val spannableWithArgs = SpannableString(textWithArgs)
    text = context.setSpans(styledSpannable, spannableWithArgs, listener)
}

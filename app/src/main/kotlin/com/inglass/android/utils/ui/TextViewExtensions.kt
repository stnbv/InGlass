package com.inglass.android.utils.ui

import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.widget.doOnTextChanged
import com.inglass.android.R
import com.inglass.android.domain.models.Country
import com.inglass.android.domain.repository.PreferencesRepository
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

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

fun TextView.getOrCreatePhoneMaskWatcher(country: Country?): MaskFormatWatcher {
    val currentWatcher = getTag(R.id.phone_mask_watcher) as? MaskFormatWatcher
    if (currentWatcher != null) return currentWatcher
    val prefs = PreferencesRepository(this.context)
    val currentCountry = country ?:  Country.RUSSIA

    val watcher = MaskFormatWatcher(
        MaskImpl.createTerminated(UnderscoreDigitSlotsParser().parseSlots(currentCountry.countryCode + " " + currentCountry.rawMask))
    ).apply {
        maskOriginal.isShowingEmptySlots = false
        maskOriginal.isForbidInputWhenFilled = true
        maskOriginal.isHideHardcodedHead = true
    }
    watcher.installOn(this)
    setTag(R.id.phone_mask_watcher, watcher)
    return watcher
}

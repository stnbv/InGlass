package com.inglass.android.utils.ui

import android.app.DatePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
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
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.color.MaterialColors
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.inglass.android.R
import java.util.Calendar
import java.util.Date

//TODO Необходимо перебрать эти утилсы, и разнести по отдельным файлам

private const val EIGHTEEN_YEARS_IN_MILLIS = 568025136000
private const val MINIMAL_AGE_TO_USE_APP = 18
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

fun Context.copyToClipboard(text: String) {
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    val clip = ClipData.newPlainText("label", text)
    clipboard!!.setPrimaryClip(clip)
}

fun Context.getClipboardContent(): String? {
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    return clipboard?.primaryClip?.getItemAt(0)?.text?.toString()
}

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
fun CharSequence.toClickableSpannable(
    context: Context,
    listener: (value: String) -> Unit,
): Spannable {
    val spannable = SpannableString(this)
    return context.setSpans(spannable, spannable, listener)
}

fun TextInputLayout.clickOnEndIcon() {
    val endIconContentDescription = this.endIconContentDescription
    val foundViews: ArrayList<View> = ArrayList()
    this.findViewsWithText(
        foundViews,
        endIconContentDescription,
        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION
    )
    foundViews.firstOrNull()?.performClick()
}

fun getDatePicker(@StringRes title: Int, selection: Long = System.currentTimeMillis()) =
    MaterialDatePicker.Builder.datePicker()
        .setTitleText(title)
        .setSelection(selection)
        .build()

fun getTimePicker(@StringRes title: Int, selection: Long = System.currentTimeMillis()): MaterialTimePicker {
    val (hour, minute) = getTimeFromUnix(selection)
    return MaterialTimePicker.Builder()
        .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)
        .setTimeFormat(TimeFormat.CLOCK_24H)
        .setTitleText(title)
        .setHour(hour)
        .setMinute(minute)
        .build()
}

fun getDatePickerDialog(button: TextInputEditText, returnBirth: (Date) -> Unit) {
    val currentTimeInMillis = Calendar.getInstance().timeInMillis
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.YEAR, MINIMAL_AGE_TO_USE_APP)
    val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            returnBirth.invoke(calendar.time)
        }

    button.doOnClick {
        val dialogDatePicker = DatePickerDialog(button.context, R.style.DatePickerDialog, dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialogDatePicker.datePicker.maxDate = currentTimeInMillis - EIGHTEEN_YEARS_IN_MILLIS
        dialogDatePicker.show()
    }
}

fun getTimeFromUnix(unixTime: Long): Pair<Int, Int> {
    val calendar = Calendar.getInstance().apply { timeInMillis = unixTime }
    return Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
}

fun String.remove(vararg oldChars: String): String {
    var temp = this
    oldChars.forEach {
        temp = temp.replace(it, "")
    }
    return temp
}

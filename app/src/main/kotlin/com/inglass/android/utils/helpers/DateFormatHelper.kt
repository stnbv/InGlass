package com.inglass.android.utils.helpers

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

enum class DateFormatHelper(private val format: String, private val utcFixed: Boolean = false) {

    SimpleDate("yyyy-MM-dd"),
    DotsDate("dd.MM.yyyy"),
    SlashedDate("dd/MM/yyyy"),
    CreateProcedureTimeStamp("yyyy-MM-dd'T'HH:mm:ssXXX"),
    FullDateTimeStamp("yyyy-MM-dd'T'HH:mm:ss"),
    FullDate("yyyy-MM-dd HH:mm:ss"),
    ProcedureCreateDate("dd MMMM yyyy"),
    ProcedureCreateDateWithTime("dd MMM yyyy HH:mm"),
    DayMonth("dd MMM"),
    SimpleTime("HH:mm"),
    AbsoluteFullTime("HH:mm:ss", true),
    FullTime("HH:mm:ss"),
    HourOfDayTime("HH"),
    HourAndMinuteForDuration("H:mm");

    private val formatter = SimpleDateFormat(format, Locale.getDefault())

    init {
        if (utcFixed) {
            formatter.timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    fun parse(string: String): Date? = formatter.parse(string)

    fun parseToCalendar(string: String): Calendar? =
        parse(string)?.let { Calendar.getInstance().apply { time = it } }

    fun format(date: Date): String = formatter.format(date)

    fun format(date: Long): String = formatter.format(date)

    fun formatCalendar(calendar: Calendar): String = formatter.format(calendar.time)
}

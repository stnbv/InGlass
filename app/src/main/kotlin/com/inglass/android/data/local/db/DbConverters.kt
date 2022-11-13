package com.inglass.android.data.local.db

import androidx.room.TypeConverter
import java.util.*

class DbConverters {
    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}

package com.inglass.android.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.math.BigDecimal
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

    @TypeConverter
    fun toListOfStrings(str: String): List<String> {
        return str.split(",")
    }

    @TypeConverter
    fun fromListOfStrings(items: List<String>): String {
        return items.joinToString(",")
    }

    @TypeConverter
    fun toBigDecimal(str: String): BigDecimal {
        return str.toBigDecimal()
    }

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String {
        return value.toString()
    }

    @TypeConverter
    fun toListOfInt(str: String): List<Int> {
        return Gson().fromJson(str, object : TypeToken<List<Int>>() {}.type)
    }

    @TypeConverter
    fun fromListOfInt(items: List<Int>): String {
        val arr = items.map { it.toString() }.toTypedArray()
        return arr.contentToString()
    }
}

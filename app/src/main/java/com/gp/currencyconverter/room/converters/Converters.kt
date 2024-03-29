package com.gp.currencyconverter.room.converters

import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

import java.util.Date

object Converters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }
}
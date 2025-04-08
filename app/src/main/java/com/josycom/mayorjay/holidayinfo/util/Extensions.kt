package com.josycom.mayorjay.holidayinfo.util

import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

fun List<String>.getJoinedString(): String = this.joinToString(", ")

fun String.getFormattedDate(fromFormat: SimpleDateFormat, toFormat: SimpleDateFormat): String {
    return try {
        toFormat.format(fromFormat.parse(this) ?: Date())
    } catch (e: ParseException) {
        Timber.e(e)
        this
    }
}
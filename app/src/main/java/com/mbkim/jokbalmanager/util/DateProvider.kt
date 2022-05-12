package com.mbkim.jokbalmanager.util

import java.text.SimpleDateFormat
import java.util.*

fun getTodayMonth(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM", Locale.KOREA)
    return dateFormat.format(Date())
}

fun getCurrentTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd-hh-mm-ss", Locale.KOREA)
    return dateFormat.format(Date())
}

fun getPreviousMonth(prevCount: Int): String {
    val dateFormat = SimpleDateFormat("yyyy-MM", Locale.KOREA)
    val cal = Calendar.getInstance()
    cal.add(Calendar.MONTH, prevCount)
    return dateFormat.format(cal.time).toString()
}

fun getPreviousYear(prevCount: Int): String {
    val dateFormat = SimpleDateFormat("yyyy", Locale.KOREA)
    val cal = Calendar.getInstance()
    cal.add(Calendar.YEAR, prevCount)
    return dateFormat.format(cal.time).toString()
}

fun getDaysOfPreviousMonthList(year: Int, month: Int): List<String> {
    val dates = mutableListOf<String>()
    val cal = Calendar.getInstance()
    cal.set(year, month - 1, 1)

    var m = month.toString()
    if (m.length == 1) m = "0$month"

    val days = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    for (i in 1..days) {
        val date: String = if (i < 10) {
            "$year-$m-0$i"
        } else {
            "$year-$m-$i"
        }
        dates.add(date)
    }
    return dates
}
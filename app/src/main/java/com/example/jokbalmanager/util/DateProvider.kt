package com.example.jokbalmanager.util

import java.text.SimpleDateFormat
import java.util.*

fun getTodayMonth(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM", Locale.KOREA)
    return dateFormat.format(Date())
}

fun getPreviousMonth(prevCount: Int): String {
    val dateFormat = SimpleDateFormat("yyyy-MM", Locale.KOREA)
    val cal = Calendar.getInstance()
    cal.add(Calendar.MONTH, prevCount)
    return dateFormat.format(cal.time).toString()
}

fun getDaysOfPreviousMonthList(year: Int, month: Int): List<String> {
    val dates = mutableListOf<String>()
    val dateFormat = SimpleDateFormat("yyyy-MM", Locale.KOREA)
    val cal = Calendar.getInstance()
    cal.set(year, month - 1, 1)
    val days = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    for (i in 1..days) {
        val date: String = if (i < 10) {
            "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-0$i"
        } else {
            "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-$i"
        }
        dates.add(date)
    }
    return dates
}
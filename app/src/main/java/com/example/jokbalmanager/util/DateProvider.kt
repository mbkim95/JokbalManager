package com.example.jokbalmanager.util

import java.text.SimpleDateFormat
import java.util.*

fun getTodayMonth(): String {
    val dateFormat = SimpleDateFormat("yyyy/MM", Locale.KOREA)
    return dateFormat.format(Date())
}

fun getPreviousMonth(prevCount: Int): String {
    val dateFormat = SimpleDateFormat("yyyy/MM", Locale.KOREA)
    val cal = Calendar.getInstance()
    cal.add(Calendar.MONTH, prevCount)
    return dateFormat.format(cal.time).toString()
}

fun getDaysOfPreviousMonth(prevCount: Int): List<String> {
    val dates = mutableListOf<String>()
    val dateFormat = SimpleDateFormat("yyyy/MM", Locale.KOREA)
    val cal = Calendar.getInstance()
    cal.add(Calendar.MONTH, prevCount)
    val days = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    for (i in 1..days) {
        val date: String = if (i < 10) {
            "${dateFormat.format(cal.time)}/0$i"
        } else {
            "${dateFormat.format(cal.time)}/$i"
        }
        dates.add(date)
    }
    return dates
}
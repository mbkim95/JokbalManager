package com.example.jokbalmanager.util

import java.text.SimpleDateFormat
import java.util.*

fun getTodayMonth(): String {
    val dateFormat = SimpleDateFormat("yyyy/MM")
    return dateFormat.format(Date())
}

fun getPreviousMonth(prevCount: Int): String {
    val dateFormat = SimpleDateFormat("yyyy/MM")
    val cal = Calendar.getInstance()
    cal.add(Calendar.MONTH, prevCount)
    return dateFormat.format(cal.time).toString()
}
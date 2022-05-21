package com.mbkim.jokbalmanager.util

import com.mbkim.jokbalmanager.model.DayOrder
import com.mbkim.jokbalmanager.model.MonthOrder
import java.util.*

fun generateDummyData(): MutableList<DayOrder> {
    val cal = Calendar.getInstance()
    val currentYear = cal.get(Calendar.YEAR)
    val currentMonth = cal.get(Calendar.MONTH) + 1
    return generateDummyData(currentYear, currentMonth)
}

fun generateDummyData(year: Int, month: Int): MutableList<DayOrder> {
    val orders = mutableListOf<DayOrder>()
    val days = getDaysOfPreviousMonthList(year, month)
    days.forEach {
        orders.add(DayOrder(it, mutableListOf()))
    }
    return orders
}

fun generateYearDummyData(): MutableList<MonthOrder> {
    val year = Calendar.getInstance().get(Calendar.YEAR)
    return MutableList(12) {
        MonthOrder("${year}-${it + 1}", 0, 0.0, 0)
    }
}
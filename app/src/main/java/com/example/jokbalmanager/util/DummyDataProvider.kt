package com.example.jokbalmanager.util

import com.example.jokbalmanager.model.DayOrder
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
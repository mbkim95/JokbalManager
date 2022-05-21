package com.mbkim.jokbalmanager.util

import com.mbkim.jokbalmanager.model.MonthOrder
import com.mbkim.jokbalmanager.model.Order
import java.util.*

fun generateDummyData(): MutableList<Order> {
    val cal = Calendar.getInstance()
    val currentYear = cal.get(Calendar.YEAR)
    val currentMonth = cal.get(Calendar.MONTH) + 1
    return generateDummyData(currentYear, currentMonth)
}

fun generateDummyData(year: Int, month: Int): MutableList<Order> {
    val orders = mutableListOf<Order>()
    val days = getDaysOfPreviousMonthList(year, month)
    days.forEach {
        orders.add(Order(it, 0, 0.0, 0))
    }
    return orders
}

fun generateYearDummyData(): MutableList<MonthOrder> {
    val year = Calendar.getInstance().get(Calendar.YEAR)
    return MutableList(12) {
        MonthOrder("${year}-${it + 1}", 0, 0.0, 0)
    }
}
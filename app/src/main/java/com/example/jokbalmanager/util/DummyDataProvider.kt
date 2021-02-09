package com.example.jokbalmanager.util

import com.example.jokbalmanager.model.DayOrder

fun generateDummyData(): List<DayOrder> {
    val orders = mutableListOf<DayOrder>()
    val days = getDaysOfPreviousMonth(0)
    days.forEach {
        orders.add(DayOrder(it, listOf()))
    }
    return orders
}
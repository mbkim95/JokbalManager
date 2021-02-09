package com.example.jokbalmanager.repository

import com.example.jokbalmanager.model.DayOrder
import com.example.jokbalmanager.model.dummyData

class OrderRepository {
    fun getMonthOrders(): List<DayOrder> {
        return dummyData
    }
}
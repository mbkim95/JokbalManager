package com.example.jokbalmanager.repository

import android.content.Context
import androidx.room.Room
import com.example.jokbalmanager.model.DayOrder
import com.example.jokbalmanager.model.db.AppDatabase
import com.example.jokbalmanager.model.db.OrderEntity
import com.example.jokbalmanager.model.dummyData
import com.example.jokbalmanager.util.generateDummyData
import java.util.*

class OrderRepository(context: Context) {
    private val db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
        .allowMainThreadQueries()
        .build()

    fun getMonthOrders(year: Int, month: Int): List<DayOrder> {
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, 1)
        val start =
            "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.DATE)}"
        val end =
            "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${
                cal.getActualMaximum(
                    Calendar.DAY_OF_MONTH
                )
            }"
        val orders = db.orderDao().getOrderData(start, end)
        return dummyData
    }

    private fun convertEntityToOrder(entity: List<OrderEntity>, year: Int, month: Int) {
        val orders = generateDummyData(year, month)
        entity.forEach {
            it.date
        }
    }

    companion object {
        const val DB_NAME = "orders.db"
    }
}
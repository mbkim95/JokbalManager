package com.example.jokbalmanager.repository

import android.content.Context
import androidx.room.Room
import com.example.jokbalmanager.model.DayOrder
import com.example.jokbalmanager.model.Jok
import com.example.jokbalmanager.model.Order
import com.example.jokbalmanager.model.db.AppDatabase
import com.example.jokbalmanager.model.db.OrderEntity
import com.example.jokbalmanager.util.generateDummyData
import java.util.*

class OrderRepository(context: Context) {
    private val db = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
        .allowMainThreadQueries()
        .build()

    fun insertOrder(order: OrderEntity) {
        db.orderDao().insertOrder(order)
    }

    fun getMonthOrders(year: Int, month: Int): List<DayOrder> {
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, 1)
        var m = month.toString()
        if (m.length == 1) m = "0$month"

        val start = "${year}-${m}-0$1"
        val end = "${year}-${m}-${cal.getActualMaximum(Calendar.DAY_OF_MONTH)}"

        val orders = db.orderDao().getOrderData(start, end)
        return convertEntityToOrder(orders, year, month)
    }

    private fun convertEntityToOrder(
        entity: List<OrderEntity>,
        year: Int,
        month: Int
    ): List<DayOrder> {
        val orders = generateDummyData(year, month)
        entity.forEach { order ->
            val day = order.date.split('-').map { it.toInt() }[2]
            val type = when (order.type) {
                0 -> Jok.FRONT
                1 -> Jok.BACK
                else -> Jok.MIX
            }
            orders[day - 1].orders.add(Order(type, order.price, order.weight, order.deposit))
        }
        return orders
    }

    companion object {
        const val DB_NAME = "orders.db"
    }
}
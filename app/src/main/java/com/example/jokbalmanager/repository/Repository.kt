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
        val searchedOrder = db.orderDao().findOrderByType(order.date, order.type)
        if (searchedOrder == null) {
            db.orderDao().insertOrder(order)
            return
        }
        db.orderDao().addSameDate(order.date, order.type, order.weight, order.deposit)
    }

    fun getMonthOrders(year: Int, month: Int): List<DayOrder> {
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, 1)
        var m = month.toString()
        if (m.length == 1) m = "0$month"

        val start = "${year}-${m}-01"
        val end = "${year}-${m}-${cal.getActualMaximum(Calendar.DAY_OF_MONTH)}"

        val orders = db.orderDao().getOrderData(start, end)
        return convertEntityToOrder(orders, year, month)
    }

    fun deleteOrder(order: OrderEntity) {
        db.orderDao().deleteOrder(order)
    }

    fun updateOrder(date: String, type: Int, order: OrderEntity) {
        val searchedOrder = db.orderDao().findOrderByType(order.date, order.type)
        // 날짜를 수정했더니 기존에 있는 품목에 추가해야하는 경우에는 수정 전 데이터를 삭제하고 기존에 있던 품목에 더해준다
        if (searchedOrder != null) {
            db.orderDao().deleteOrder(OrderEntity(date, type, order.price, order.weight, order.deposit))
            db.orderDao().addSameDate(order.date, order.type, order.weight, order.deposit)
            return
        }
        db.orderDao().updateOrder(date, type, order.date, order.price, order.weight, order.deposit)
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
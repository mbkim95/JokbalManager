package com.mbkim.jokbalmanager.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.room.Room
import com.mbkim.jokbalmanager.model.DayOrder
import com.mbkim.jokbalmanager.model.Jok
import com.mbkim.jokbalmanager.model.MonthOrder
import com.mbkim.jokbalmanager.model.Order
import com.mbkim.jokbalmanager.model.db.AppDatabase
import com.mbkim.jokbalmanager.model.db.OrderEntity
import com.mbkim.jokbalmanager.util.generateDummyData
import com.mbkim.jokbalmanager.util.getCurrentTime
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.math.BigDecimal
import java.util.*

class OrderRepository private constructor(context: Context) {
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

    fun getYearOrders(year: Int): List<MonthOrder> {
        var orderData = db.orderDao().getOrderData("${year}-01-01", "${year}-12-31")
        val yearOrders = MutableList<MonthOrder>(12) {
            MonthOrder("${year}-${it + 1}", 0, 0.0, 0)
        }
        orderData.forEach {
            val month = it.date.substring(5, 7).toInt()
            yearOrders[month - 1].apply {
                val totalPrice =
                    BigDecimal.valueOf(it.price).multiply(BigDecimal.valueOf(it.weight)).toLong()
                price += totalPrice
                weight = BigDecimal.valueOf(weight).add(BigDecimal.valueOf(it.weight)).toDouble()
                balance += (totalPrice - it.deposit)
            }
        }
        return yearOrders
    }

    fun deleteOrder(order: OrderEntity) {
        db.orderDao().deleteOrder(order)
    }

    fun updateOrder(date: String, type: Int, order: OrderEntity) {
        val searchedOrder = db.orderDao().findOrderByType(order.date, order.type)
        // 날짜를 수정했더니 기존에 있는 품목에 추가해야하는 경우에는 수정 전 데이터를 삭제하고 기존에 있던 품목에 더해준다
        if (date != order.date && type == order.type && searchedOrder != null) {
            with(db.orderDao()) {
                deleteOrder(OrderEntity(date, type, order.price, order.weight, order.deposit))
                addSameDate(order.date, order.type, order.weight, order.deposit)
            }
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

    fun saveDatabase() {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath,
                    "jokbal-${getCurrentTime()}.db"
                )

                db.close()

                FileInputStream(File(db.openHelper.writableDatabase.path)).use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    fun restoreDatabase(contentResolver: ContentResolver, uri: Uri) {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                val file = File(db.openHelper.writableDatabase.path)

                db.close()

                contentResolver.openInputStream(uri).use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        inputStream?.copyTo(outputStream)
                    }
                }
            }

        } catch(e: Exception) {
            throw e
        }
    }

    companion object {
        const val DB_NAME = "orders.db"

        @Volatile
        private var instance: OrderRepository? = null

        @JvmStatic
        fun getInstance(context: Context): OrderRepository =
            instance ?: synchronized(this) {
                instance ?: OrderRepository(context).also {
                    instance = it
                }
            }
    }
}
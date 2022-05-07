package com.mbkim.jokbalmanager.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {
    @Insert
    fun insertOrder(order: OrderEntity)

    @Query("SELECT * FROM orders WHERE date BETWEEN :start AND :end")
    fun getOrderData(start: String, end: String): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE date = :date AND type = :type")
    fun findOrderByType(date: String, type: Int): OrderEntity?

    @Query("UPDATE orders SET weight = weight + :weight, deposit = deposit + :deposit WHERE date = :date AND type = :type")
    fun addSameDate(date: String, type: Int, weight: Double, deposit: Long)

    @Delete
    fun deleteOrder(order: OrderEntity)

    @Query("UPDATE orders SET date = :date, price = :price, weight = :weight, deposit = :deposit  WHERE date = :prevDate AND type = :type")
    fun updateOrder(prevDate: String, type: Int, date: String, price: Long, weight: Double, deposit: Long)
}
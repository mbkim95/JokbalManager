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

    @Query("SELECT * FROM orders WHERE date = :date")
    fun findOrderByDate(date: String): OrderEntity?

    @Query("UPDATE orders SET weight = weight + :weight, deposit = deposit + :deposit WHERE date = :date")
    fun addSameDate(date: String, weight: Double, deposit: Long)

    @Delete
    fun deleteOrder(order: OrderEntity)

    @Query("UPDATE orders SET date = :date, price = :price, weight = :weight, deposit = :deposit  WHERE date = :prevDate")
    fun updateOrder(prevDate: String, date: String, price: Long, weight: Double, deposit: Long)
}
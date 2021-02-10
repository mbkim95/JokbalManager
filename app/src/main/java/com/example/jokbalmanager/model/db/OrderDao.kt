package com.example.jokbalmanager.model.db

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
    fun updateOrderData(date: String, type: Int, weight: Double, deposit: Int)

    @Delete
    fun deleteOrder(order: OrderEntity)
}
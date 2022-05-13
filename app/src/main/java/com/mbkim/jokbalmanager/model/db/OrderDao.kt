package com.mbkim.jokbalmanager.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: OrderEntity)

    @Query("SELECT * FROM orders WHERE date BETWEEN :start AND :end")
    suspend fun getOrderData(start: String, end: String): List<OrderEntity>

    @Query("SELECT * FROM orders WHERE date = :date AND type = :type")
    suspend fun findOrderByType(date: String, type: Int): OrderEntity?

    @Query("UPDATE orders SET weight = weight + :weight, deposit = deposit + :deposit WHERE date = :date AND type = :type")
    suspend fun addSameDate(date: String, type: Int, weight: Double, deposit: Long)

    @Delete
    suspend fun deleteOrder(order: OrderEntity)

    @Query("UPDATE orders SET date = :date, price = :price, weight = :weight, deposit = :deposit  WHERE date = :prevDate AND type = :type")
    suspend fun updateOrder(prevDate: String, type: Int, date: String, price: Long, weight: Double, deposit: Long)
}
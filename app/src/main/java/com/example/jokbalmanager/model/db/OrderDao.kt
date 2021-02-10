package com.example.jokbalmanager.model.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {
    @Insert
    fun insertOrder(order: OrderEntity)

    @Query("SELECT * FROM orders WHERE date BETWEEN :start AND :end")
    fun getOrderData(start: String, end: String): List<OrderEntity>
}
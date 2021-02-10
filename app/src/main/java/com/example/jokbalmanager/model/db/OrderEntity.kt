package com.example.jokbalmanager.model.db

import androidx.room.Entity

@Entity(tableName = "orders", primaryKeys = ["date", "type"])
data class OrderEntity(
    val date: String,
    val type: Int,
    val price: Int,
    val weight: Double,
    val deposit: Int
)

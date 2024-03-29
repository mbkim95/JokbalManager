package com.mbkim.jokbalmanager.model.db

import androidx.room.Entity

@Entity(tableName = "orders", primaryKeys = ["date", "type"])
data class OrderEntity(
    val date: String,
    val type: Int,
    val price: Long,
    val weight: Double,
    val deposit: Long
)

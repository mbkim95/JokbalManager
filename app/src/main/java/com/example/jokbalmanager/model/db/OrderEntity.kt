package com.example.jokbalmanager.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    val date: String,
    val type: Int,
    val price: Int,
    val weight: Double,
    val deposit: Int,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
)

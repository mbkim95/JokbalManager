package com.example.jokbalmanager.model

enum class Jok {
    FRONT, BACK, MIX
}

data class Order(
    var type: Jok,
    var price: Int,
    var weight: Double,
    var deposit: Int
)
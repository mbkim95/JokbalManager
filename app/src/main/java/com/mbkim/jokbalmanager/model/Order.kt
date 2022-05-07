package com.mbkim.jokbalmanager.model

enum class Jok {
    FRONT, BACK, MIX
}

data class Order(
    var type: Jok,
    var price: Long,
    var weight: Double,
    var deposit: Long
)

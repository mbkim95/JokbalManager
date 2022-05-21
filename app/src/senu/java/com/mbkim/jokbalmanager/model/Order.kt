package com.mbkim.jokbalmanager.model

data class Order(
    var date: String,
    var price: Long,
    var weight: Double,
    var deposit: Long
)

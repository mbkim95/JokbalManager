package com.example.jokbalmanager.util

import org.junit.Test

class DateProviderKtTest {
    @Test
    fun daysOfMonthTest() {
        val days = getDaysOfPreviousMonth(-12)
        println(days)
    }
}
package com.example.jokbalmanager.util

import org.junit.Test
import java.util.*

class DateProviderKtTest {
    @Test
    fun daysOfMonthTest() {
        val days = getDaysOfPreviousMonthList(2021, 2)
        println(days)
    }

    @Test
    fun calendarTest() {
        val cal = Calendar.getInstance()
        println("${cal.get(Calendar.YEAR)}, ${cal.get(Calendar.MONTH) + 1}")
    }
}
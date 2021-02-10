package com.example.jokbalmanager.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jokbalmanager.model.db.AppDatabase
import com.example.jokbalmanager.model.db.OrderDao
import com.example.jokbalmanager.model.db.OrderEntity
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class OrderRepositoryTest {
    private lateinit var orderDao: OrderDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        orderDao = db.orderDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun dbTest() {
        orderDao.insertOrder(OrderEntity("2021-02-10", 1, 1.8, 3000))
        orderDao.insertOrder(OrderEntity("2021-02-01", 1, 1.8, 5000))
        orderDao.insertOrder(OrderEntity("2021-02-12", 3, 2.8, 6700))
        orderDao.insertOrder(OrderEntity("2021-02-01", 2, 1.8, 3000))
        orderDao.insertOrder(OrderEntity("2021-01-03", 0, 3.2, 64000))
        val order = orderDao.getOrderData("2021-02-01", "2021-02-28")
        assertEquals(4, order.size)
    }
}
package com.example.jokbalmanager.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jokbalmanager.model.db.AppDatabase
import com.example.jokbalmanager.model.db.OrderDao
import com.example.jokbalmanager.model.db.OrderEntity
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class OrderRepositoryTest {
    private lateinit var orderDao: OrderDao
    private lateinit var db: AppDatabase
    private lateinit var repository: OrderRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        repository = OrderRepository(context)
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
        orderDao.insertOrder(OrderEntity("2021-02-10", 1, 10000, 1.8, 20000))
        orderDao.insertOrder(OrderEntity("2021-02-01", 1, 20000, 1.8, 52000))
        orderDao.insertOrder(OrderEntity("2021-02-20", 2, 19000, 1.4, 170000))
        orderDao.insertOrder(OrderEntity("2021-02-10", 1, 15000, 2.0, 19000))
        orderDao.insertOrder(OrderEntity("2021-02-14", 0, 10000, 1.8, 23000))

        val order = orderDao.getOrderData("2021-02-01", "2021-02-28")
        val testOrder = orderDao.findOrderByType("2021-02-14", 1)
        assertNull(testOrder)
        assertEquals(5, order.size)
    }

    @Test
    fun updateTest() {
        orderDao.insertOrder(OrderEntity("2021-02-14", 0, 10000, 1.8, 23000))
        orderDao.updateOrderData("2021-02-14", 0, 1.2, 7000)
        val order = orderDao.findOrderByType("2021-02-14", 0)
        order?.let {
            assertEquals(3.0, it.weight)
            assertEquals(30000, it.deposit)
        }
    }

    @Test
    fun deleteTest() {
        orderDao.insertOrder(OrderEntity("2021-02-14", 0, 10000, 1.8, 23000))
        orderDao.deleteOrder(OrderEntity("2021-02-14", 0, 10000, 1.8, 23000))
        val order = orderDao.findOrderByType("2021-02-14", 0)
        assertNull(order)
    }
}
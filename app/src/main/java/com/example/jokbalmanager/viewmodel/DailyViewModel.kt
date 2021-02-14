package com.example.jokbalmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.jokbalmanager.model.DayOrder
import com.example.jokbalmanager.model.db.OrderEntity
import com.example.jokbalmanager.repository.OrderRepository
import java.util.*

class DailyViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = OrderRepository.getInstance(application)

    private val _count = MutableLiveData(0)
    val count: LiveData<Int> get() = _count

    private val _monthOrders = MutableLiveData<List<DayOrder>>()
    val monthOrders: MutableLiveData<List<DayOrder>> get() = _monthOrders

    private val _totalWeight = MutableLiveData<Double>()
    val totalWeight: LiveData<Double> get() = _totalWeight
    private val _totalPrice = MutableLiveData<Long>()
    val totalPrice: LiveData<Long> get() = _totalPrice
    private val _totalBalance = MutableLiveData<Long>()
    val totalBalance: LiveData<Long> get() = _totalBalance

    private var month: Int
    private var year: Int

    init {
        val cal = Calendar.getInstance()
        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH) + 1
    }

    fun moveNextMonth() {
        if (month == 12) {
            year++
            month = 1
        } else {
            month++
        }
        _count.value = _count.value?.plus(1)
        getMonthOrderData()
    }

    fun movePrevMonth() {
        if (month == 1) {
            year--
            month = 12
        } else {
            month--
        }
        _count.value = _count.value?.minus(1)
        getMonthOrderData()
    }

    fun setDate(date: String) {
        val newYear = date.substring(0, 4).toInt()
        val newMonth = date.substring(5, 7).toInt()
        val diff = (newYear - year) * 12 + (newMonth - month)
        _count.value = _count.value?.plus(diff)
        year = newYear
        month = newMonth
        getMonthOrderData()
    }

    fun addOrderData(order: OrderEntity) {
        repository.insertOrder(order)
        getMonthOrderData()
    }

    fun deleteOrder(order: OrderEntity) {
        repository.deleteOrder(order)
        getMonthOrderData()
    }

    fun updateOrder(prevDate: String, type: Int, order: OrderEntity) {
        repository.updateOrder(prevDate, type, order)
        getMonthOrderData()
    }

    fun getMonthOrderData() {
        var weights = 0.0
        var prices = 0L
        var balances = 0L
        val orders = repository.getMonthOrders(year, month)
        orders.forEach {
            it.orders.forEach { order ->
                weights += order.weight
                prices += (order.price * order.weight).toLong()
                balances += (order.price * order.weight - order.deposit).toLong()
            }
        }
        _monthOrders.value = orders
        _totalWeight.value = weights
        _totalPrice.value = prices
        _totalBalance.value = balances
    }
}
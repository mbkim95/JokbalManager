package com.mbkim.jokbalmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mbkim.jokbalmanager.model.DayOrder
import com.mbkim.jokbalmanager.model.db.OrderEntity
import com.mbkim.jokbalmanager.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertOrder(order)
            getMonthOrderData()
        }
    }

    fun deleteOrder(order: OrderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteOrder(order)
            getMonthOrderData()
        }
    }

    fun updateOrder(prevDate: String, type: Int, order: OrderEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateOrder(prevDate, type, order)
            getMonthOrderData()
        }
    }

    fun getMonthOrderData() {
        viewModelScope.launch(Dispatchers.IO) {
            var weights = BigDecimal.valueOf(0.0)
            var prices = 0L
            var balances = 0L
            val orders = repository.getMonthOrders(year, month)
            orders.forEach {
                it.orders.forEach { order ->
                    val price =
                        BigDecimal.valueOf(order.price).multiply(BigDecimal.valueOf(order.weight))
                            .toLong()
                    weights = weights.add(BigDecimal.valueOf(order.weight))
                    prices += price
                    balances += price - order.deposit
                }
            }
            withContext(Dispatchers.Main) {
                _monthOrders.value = orders
                _totalWeight.value = weights.toDouble()
                _totalPrice.value = prices
                _totalBalance.value = balances
            }
        }
    }
}
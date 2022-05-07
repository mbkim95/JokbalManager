package com.mbkim.jokbalmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mbkim.jokbalmanager.model.MonthOrder
import com.mbkim.jokbalmanager.repository.OrderRepository
import java.math.BigDecimal
import java.util.*

class TotalViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = OrderRepository.getInstance(application)

    private val _count = MutableLiveData(0)
    val count: LiveData<Int> get() = _count

    private val _yearOrders = MutableLiveData<List<MonthOrder>>()
    val yearOrder: MutableLiveData<List<MonthOrder>> get() = _yearOrders

    private val _totalWeight = MutableLiveData<Double>()
    val totalWeight: LiveData<Double> get() = _totalWeight
    private val _totalPrice = MutableLiveData<Long>()
    val totalPrice: LiveData<Long> get() = _totalPrice
    private val _totalBalance = MutableLiveData<Long>()
    val totalBalance: LiveData<Long> get() = _totalBalance

    private var year: Int

    init {
        val cal = Calendar.getInstance()
        year = cal.get(Calendar.YEAR)
    }

    fun moveNextYear() {
        year++
        _count.value = _count.value?.plus(1)
        getYearOrderData()
    }

    fun movePrevYear() {
        year--
        _count.value = _count.value?.minus(1)
        getYearOrderData()
    }

    fun setYear(date: String) {
        val diff = date.toInt() - year
        year = date.toInt()
        _count.value = _count.value?.plus(diff)
        getYearOrderData()
    }

    fun getYearOrderData() {
        var weights = BigDecimal.valueOf(0.0)
        var prices = 0L
        var balances = 0L

        val yearOrders = repository.getYearOrders(year)
        yearOrders.forEach {
            weights = weights.add(BigDecimal.valueOf(it.weight))
            prices += it.price
            balances += it.balance
        }
        _yearOrders.value = yearOrders
        _totalWeight.value = weights.toDouble()
        _totalPrice.value = prices
        _totalBalance.value = balances
    }
}
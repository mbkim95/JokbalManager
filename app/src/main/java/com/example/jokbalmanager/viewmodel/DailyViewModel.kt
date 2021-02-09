package com.example.jokbalmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jokbalmanager.model.DayOrder
import com.example.jokbalmanager.repository.OrderRepository

class DailyViewModel : ViewModel() {
    private val repository = OrderRepository()

    private val _count = MutableLiveData(0)
    val count: LiveData<Int> get() = _count

    private val _monthOrders = MutableLiveData<List<DayOrder>>()
    val monthOrders: MutableLiveData<List<DayOrder>> get() = _monthOrders

    fun moveNextMonth() {
        _count.value = _count.value?.plus(1)
    }

    fun movePrevMonth() {
        _count.value = _count.value?.minus(1)
    }

    fun getMonthOrderData() {
        _monthOrders.value = repository.getMonthOrders()
    }
}
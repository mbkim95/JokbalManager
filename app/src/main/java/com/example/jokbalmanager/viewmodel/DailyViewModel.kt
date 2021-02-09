package com.example.jokbalmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DailyViewModel : ViewModel() {
    private val _count = MutableLiveData(0)
    val count: LiveData<Int> get() = _count

    fun moveNextMonth() {
        _count.value = _count.value?.plus(1)
    }

    fun movePrevMonth() {
        _count.value = _count.value?.minus(1)
    }
}
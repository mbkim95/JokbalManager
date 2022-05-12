package com.mbkim.jokbalmanager.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mbkim.jokbalmanager.repository.OrderRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = OrderRepository.getInstance(application)

    private val _errorMessage = MutableLiveData("")
    private val _isCompleted = MutableLiveData(false)

    val errorMessage: LiveData<String> get() = _errorMessage
    val isCompleted: LiveData<Boolean> get() = _isCompleted

    fun saveDatabase() {
        try {
            repository.saveDatabase()
            _isCompleted.value = true
            _isCompleted.value = false
        } catch (e: Exception) {
            _errorMessage.value = e.message
            _errorMessage.value = ""
        }
    }

    fun restoreDatabase(contentResolver: ContentResolver, uri: Uri) {
        try {
            repository.restoreDatabase(contentResolver, uri)
            _isCompleted.value = true
            _isCompleted.value = false
        } catch (e: Exception) {
            _errorMessage.value = e.message
            _errorMessage.value = ""
        }
    }
}
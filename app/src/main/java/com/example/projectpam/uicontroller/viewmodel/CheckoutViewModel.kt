package com.example.projectpam.uicontroller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpam.modeldata.request.CheckoutRequest
import com.example.projectpam.repositori.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val repository: OrderRepository
) : ViewModel() {

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun checkout(address: String, method: String, provider: String?) {
        viewModelScope.launch {
            try {
                repository.checkout(
                    CheckoutRequest(address, method, provider)
                )
                _success.value = true
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

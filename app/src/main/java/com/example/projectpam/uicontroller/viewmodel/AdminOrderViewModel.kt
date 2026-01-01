package com.example.projectpam.uicontroller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpam.modeldata.Order
import com.example.projectpam.repositori.AdminOrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminOrderViewModel(
    private val repository: AdminOrderRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun loadOrders() {
        viewModelScope.launch {
            try {
                _orders.value = repository.getAllOrders()
            } catch (e: Exception) {
                _message.value = "Gagal memuat pesanan"
            }
        }
    }

    fun updateStatus(orderId: Int, status: String) {
        viewModelScope.launch {
            try {
                repository.updateOrderStatus(orderId, status)
                _message.value = "Status pesanan berhasil diperbarui"
                loadOrders()
            } catch (e: Exception) {
                _message.value = "Gagal memperbarui status"
            }
        }
    }

    fun clearMessage() {
        _message.value = ""
    }
}

package com.example.projectpam.uicontroller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpam.modeldata.Cart
import com.example.projectpam.repositori.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<Cart>>(emptyList())
    val cartItems: StateFlow<List<Cart>> = _cartItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadCart() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _cartItems.value = repository.getCart()
                _errorMessage.value = null
            } catch (e: Exception) {
                _cartItems.value = emptyList()
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToCart(productId: Int, amount: Int = 1) {
        viewModelScope.launch {
            try {
                repository.addToCart(productId, amount)
                loadCart()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun increase(productId: Int) {
        viewModelScope.launch {
            try {
                repository.increaseQty(productId)
                loadCart()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun decrease(productId: Int) {
        viewModelScope.launch {
            try {
                repository.decreaseQty(productId)
                loadCart()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun remove(productId: Int) {
        viewModelScope.launch {
            try {
                repository.removeItem(productId)
                loadCart()
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    fun totalPrice(): Double {
        return cartItems.value.sumOf { cart ->
            val price = cart.product?.price
                ?.toString()
                ?.toDoubleOrNull() ?: 0.0

            price * cart.quantity
        }
    }
}

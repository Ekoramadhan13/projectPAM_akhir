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
    val cartItems: StateFlow<List<Cart>> get() = _cartItems

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    // Load semua item di cart
    fun loadCart() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                _cartItems.value = repository.getCart()
                _errorMessage.value = null
            } catch (e: Exception) {
                _cartItems.value = emptyList()
                _errorMessage.value = e.message ?: "Gagal load cart"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Tambah produk ke cart
    fun addToCart(productId: Int, amount: Int = 1) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.addToCart(productId, amount)
                loadCart() // refresh cart setelah add
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Gagal tambah ke cart"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Tambah quantity
    fun increase(productId: Int) {
        updateCart(productId, "add")
    }

    // Kurangi quantity
    fun decrease(productId: Int) {
        updateCart(productId, "reduce")
    }

    // Hapus item dari cart
    fun remove(productId: Int) {
        updateCart(productId, "remove")
    }

    private fun updateCart(productId: Int, action: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                when (action) {
                    "add", "reduce" -> repository.updateCart(productId, action)
                    "remove" -> repository.removeCart(productId)
                }
                loadCart() // refresh cart setelah update
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Gagal update cart"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Hitung total harga
    fun totalPrice(): Double {
        return cartItems.value.sumOf {
            val price = it.product?.price?.toDoubleOrNull() ?: 0.0
            price * it.quantity
        }
    }
}

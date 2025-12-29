package com.example.projectpam.uicontroller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpam.modeldata.Product
import com.example.projectpam.repositori.ProductUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductUserViewModel(private val repository: ProductUserRepository) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    // Load semua produk
    fun loadProducts() {
        viewModelScope.launch {
            try {
                val productList = repository.getUserProducts()
                _products.value = productList
            } catch (e: Exception) {
                _products.value = emptyList()
            }
        }
    }

    // Search produk berdasarkan query dan kategori
    fun searchProducts(query: String, category: String? = null) {
        viewModelScope.launch {
            try {
                val productList = repository.searchUserProducts(query, category)
                _products.value = productList
            } catch (e: Exception) {
                _products.value = emptyList()
            }
        }
    }

    suspend fun getProductDetail(id: Int): Product? {
        return try {
            repository.getUserProductDetail(id)
        } catch (e: Exception) {
            null
        }
    }
}

package com.example.projectpam.uicontroller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpam.modeldata.Product
import com.example.projectpam.repositori.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class AdminProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    fun loadProducts() {
        viewModelScope.launch {
            try { _products.value = repository.getAllProducts() }
            catch (e: Exception) { _message.value = "Gagal memuat produk: ${e.localizedMessage}" }
        }
    }

    fun addOrUpdateProductMultipart(
        name: String,
        description: String,
        price: Double,
        stock: Int,
        category: String,
        imagePart: MultipartBody.Part?,
        isUpdate: Boolean,
        productId: Int? = null
    ) {
        viewModelScope.launch {
            try {
                if (isUpdate) {
                    if (productId == null || productId <= 0) {
                        _message.value = "ID produk tidak valid"
                        return@launch
                    }
                    repository.updateProductMultipart(productId, name, description, price, stock, category, imagePart)
                    _message.value = "Produk berhasil diperbarui"
                } else {
                    repository.addProductMultipart(name, description, price, stock, category, imagePart)
                    _message.value = "Produk berhasil ditambahkan"
                }
                loadProducts()
            } catch (e: Exception) {
                _message.value = "Gagal menyimpan produk: ${e.localizedMessage}"
            }
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteProduct(id)
                _message.value = "Produk berhasil dihapus"
                loadProducts()
            } catch (e: Exception) {
                _message.value = "Gagal menghapus produk: ${e.localizedMessage}"
            }
        }
    }
}

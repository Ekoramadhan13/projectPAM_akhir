package com.example.projectpam.uicontroller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectpam.modeldata.Product
import com.example.projectpam.repositori.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class AdminProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> get() = _products

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> get() = _message

    // ====================== Load Semua Produk ======================
    fun loadProducts() {
        viewModelScope.launch {
            try {
                _products.value = repository.getAllProducts()
            } catch (e: Exception) {
                _message.value = "Gagal memuat produk: ${e.localizedMessage}"
            }
        }
    }

    // ====================== Tambah / Update Produk via Multipart ======================
    fun addOrUpdateProductMultipart(
        name: String,
        description: String,
        price: Double,
        stock: Int,
        category: String,
        imageFile: File?,
        isUpdate: Boolean,
        productId: Int = 0
    ) {
        viewModelScope.launch {
            try {
                if (isUpdate) {
                    repository.updateProductMultipart(
                        id = productId,
                        name = name,
                        description = description,
                        price = price,
                        stock = stock,
                        category = category,
                        imageFile = imageFile
                    )
                    _message.value = "Produk berhasil diperbarui"
                } else {
                    repository.addProductMultipart(
                        name = name,
                        description = description,
                        price = price,
                        stock = stock,
                        category = category,
                        imageFile = imageFile
                    )
                    _message.value = "Produk berhasil ditambahkan"
                }
                loadProducts()
            } catch (e: Exception) {
                _message.value = "Gagal menyimpan produk: ${e.localizedMessage}"
            }
        }
    }

    // ====================== Hapus Produk ======================
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

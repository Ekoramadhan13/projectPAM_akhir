package com.example.projectpam.repositori

import com.example.projectpam.apiservice.ServiceApiEcommerce
import com.example.projectpam.modeldata.Product
import com.example.projectpam.utils.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProductRepository(
    private val api: ServiceApiEcommerce,
    private val sessionManager: SessionManager
) {

    private fun getAuthToken(): String {
        val token = sessionManager.getToken() ?: throw Exception("User belum login")
        return "Bearer $token"
    }

    // ================= GET ALL PRODUCTS =================
    suspend fun getAllProducts(): List<Product> = api.getAllProducts()

    // ================= ADD PRODUCT =================
    suspend fun addProductMultipart(
        name: String,
        description: String,
        price: Double,
        stock: Int,
        category: String,
        imagePart: MultipartBody.Part?
    ): Product {
        val token = getAuthToken()

        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryPart = category.toRequestBody("text/plain".toMediaTypeOrNull())

        return api.addProduct(namePart, descPart, pricePart, stockPart, categoryPart, imagePart, token)
    }

    // ================= UPDATE PRODUCT =================
    suspend fun updateProductMultipart(
        id: Int,
        name: String,
        description: String,
        price: Double,
        stock: Int,
        category: String,
        imagePart: MultipartBody.Part?
    ): Product {
        if (id <= 0) throw Exception("ID produk tidak valid")
        val token = getAuthToken()

        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryPart = category.toRequestBody("text/plain".toMediaTypeOrNull())

        return api.updateProduct(id, namePart, descPart, pricePart, stockPart, categoryPart, imagePart, token)
    }

    // ================= DELETE PRODUCT =================
    suspend fun deleteProduct(id: Int) {
        if (id <= 0) throw Exception("ID produk tidak valid")
        val token = getAuthToken()
        api.deleteProduct(id, token)
    }
}

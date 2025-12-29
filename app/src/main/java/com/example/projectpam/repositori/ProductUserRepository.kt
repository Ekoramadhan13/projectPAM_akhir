package com.example.projectpam.repositori

import com.example.projectpam.apiservice.ServiceApiEcommerce
import com.example.projectpam.modeldata.Product
import com.example.projectpam.utils.SessionManager

class ProductUserRepository(
    private val api: ServiceApiEcommerce,
    private val sessionManager: SessionManager
) {

    private fun getAuthToken(): String {
        val token = sessionManager.getToken() ?: throw Exception("User belum login")
        return "Bearer $token"
    }

    // Ambil semua produk user
    suspend fun getUserProducts(): List<Product> {
        val token = getAuthToken()
        return api.getUserProducts() // pastikan endpoint user butuh token di backend
    }

    // Search produk user
    suspend fun searchUserProducts(query: String, category: String? = null): List<Product> {
        val token = getAuthToken()
        return api.searchUserProducts(query, category)
    }

    // Detail produk user
    suspend fun getUserProductDetail(id: Int): Product {
        val token = getAuthToken()
        return api.getUserProductDetail(id)
    }
}

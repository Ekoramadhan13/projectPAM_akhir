package com.example.projectpam.repositori

import com.example.projectpam.apiservice.ServiceApiEcommerce
import com.example.projectpam.modeldata.Cart
import com.example.projectpam.modeldata.request.AddCartRequest
import com.example.projectpam.modeldata.response.CartListResponse
import com.example.projectpam.modeldata.response.CartResponse
import com.example.projectpam.utils.SessionManager

class CartRepository(
    private val api: ServiceApiEcommerce,
    private val sessionManager: SessionManager
) {

    private fun getAuthToken(): String {
        val token = sessionManager.getToken()
            ?: throw Exception("User belum login")
        return "Bearer $token"
    }

    // Ambil semua cart
    suspend fun getCart(): List<Cart> {
        val token = getAuthToken()
        val response: CartListResponse = api.getCart(token)
        return response.data
    }

    // Tambah item ke cart
    suspend fun addToCart(productId: Int, amount: Int = 1): CartResponse {
        val token = getAuthToken()
        return api.addToCart(token, AddCartRequest(productId, amount))
    }

    // Update quantity / hapus
    suspend fun updateCart(productId: Int, action: String): CartResponse {
        val token = getAuthToken()
        return api.updateCart(token, productId, action)
    }

    // Hapus item
    suspend fun removeCart(productId: Int) {
        val token = getAuthToken()
        api.removeCart(token, productId)
    }
}

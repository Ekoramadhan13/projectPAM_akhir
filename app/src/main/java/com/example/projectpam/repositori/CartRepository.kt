package com.example.projectpam.repositori

import com.example.projectpam.apiservice.ServiceApiEcommerce
import com.example.projectpam.modeldata.Cart
import com.example.projectpam.modeldata.request.AddCartRequest
import com.example.projectpam.modeldata.request.UpdateCartRequest
import com.example.projectpam.utils.SessionManager

class CartRepository(
    private val api: ServiceApiEcommerce,
    private val sessionManager: SessionManager
) {

    private fun token(): String =
        "Bearer ${
            sessionManager.getToken()
                ?: throw Exception("User belum login")
        }"

    suspend fun getCart(): List<Cart> {
        return api.getCart(token()).data
    }

    suspend fun addToCart(productId: Int, amount: Int = 1) {
        api.addToCart(
            token(),
            AddCartRequest(
                product_id = productId,
                amount = amount
            )
        )
    }

    suspend fun increaseQty(productId: Int) {
        api.updateCart(
            token(),
            UpdateCartRequest(
                product_id = productId,
                action = "add",
                amount = 1
            )
        )
    }

    suspend fun decreaseQty(productId: Int) {
        api.updateCart(
            token(),
            UpdateCartRequest(
                product_id = productId,
                action = "reduce",
                amount = 1
            )
        )
    }

    suspend fun removeItem(productId: Int) {
        api.removeCart(
            token(),
            UpdateCartRequest(
                product_id = productId,
                action = "remove",
                amount = 0
            )
        )
    }
}
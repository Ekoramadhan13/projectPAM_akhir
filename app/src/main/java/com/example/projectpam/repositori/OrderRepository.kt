package com.example.projectpam.repositori

import com.example.projectpam.apiservice.ServiceApiEcommerce
import com.example.projectpam.modeldata.Order
import com.example.projectpam.modeldata.request.CheckoutRequest
import com.example.projectpam.utils.SessionManager

class OrderRepository(
    private val api: ServiceApiEcommerce,
    private val session: SessionManager
) {

    private fun token() =
        "Bearer ${session.getToken() ?: throw Exception("User belum login")}"

    suspend fun checkout(request: CheckoutRequest): Order {
        return api.checkout(token(), request).data
    }

    suspend fun getMyOrders(): List<Order> {
        return api.getMyOrders(token()).data
    }
}

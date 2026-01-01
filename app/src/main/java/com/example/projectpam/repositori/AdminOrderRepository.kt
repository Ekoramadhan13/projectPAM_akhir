package com.example.projectpam.repositori

import com.example.projectpam.apiservice.ServiceApiEcommerce
import com.example.projectpam.modeldata.Order
import com.example.projectpam.modeldata.request.UpdateOrderStatusRequest
import com.example.projectpam.utils.SessionManager

class AdminOrderRepository(
    private val api: ServiceApiEcommerce,
    private val session: SessionManager
) {

    private fun token() =
        "Bearer ${session.getToken() ?: throw Exception("Admin belum login")}"

    suspend fun getAllOrders(): List<Order> {
        return api.getAllOrders(token()).data
    }

    suspend fun updateOrderStatus(orderId: Int, status: String) {
        val request = UpdateOrderStatusRequest(orderId, status)
        api.updateOrderStatus(token(), request)
    }
}

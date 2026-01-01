package com.example.projectpam.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val order_id: Int,
    val user_id: Int,
    val total_price: Double,
    val payment_method: String,
    val payment_provider: String?,
    val status: String,
    val address: String,
    val created_at: String,
    val items: List<OrderItem> = emptyList()
)

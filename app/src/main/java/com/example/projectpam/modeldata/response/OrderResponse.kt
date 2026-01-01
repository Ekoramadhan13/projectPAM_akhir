package com.example.projectpam.modeldata.response

import com.example.projectpam.modeldata.Order
import kotlinx.serialization.Serializable

@Serializable
data class OrderResponse(
    val message: String,
    val data: Order
)

@Serializable
data class OrderListResponse(
    val data: List<Order>
)

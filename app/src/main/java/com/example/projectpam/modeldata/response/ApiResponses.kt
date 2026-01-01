package com.example.projectpam.modeldata.response

import com.example.projectpam.modeldata.Cart
import com.example.projectpam.modeldata.Order
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val message: String? = null,
    val data: T
)

@Serializable
data class LoginResponse(
    val token: String,
    val role: String,
    val message: String
)
@Serializable
data class CartListResponse(
    val data: List<Cart>
)

//@Serializable
//data class CartResponse(
//val message: String,
//val data: Cart
//)

@Serializable
data class MessageResponse(
    val message: String
)

@Serializable
data class OrderResponse(
    val message: String,
    val data: Order
)

@Serializable
data class OrderListResponse(
    val data: List<Order>
)



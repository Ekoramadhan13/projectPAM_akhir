package com.example.projectpam.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val order_item_id: Int,
    val order_id: Int,
    val product_id: Int,
    val quantity: Int,
    val price: Double,
    val product: Product? = null
)

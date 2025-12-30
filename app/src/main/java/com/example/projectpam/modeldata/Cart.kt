package com.example.projectpam.modeldata

import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@KSerializable
data class Cart(
    val cart_id: Int,
    val product_id: Int,
    val user_id: Int,
    val quantity: Int,
    val product: Product? = null // nullable supaya aman jika backend gagal
) : Serializable

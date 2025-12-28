package com.example.projectpam.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val product_id: Int = 0,
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val category: String = "",
    val image_url: String = "",
    val created_at: String = "",
    val updated_at: String = ""
)

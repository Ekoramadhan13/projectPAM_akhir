package com.example.projectpam.modeldata

import java.io.Serializable
import kotlinx.serialization.Serializable as KSerializable

@KSerializable
data class Product(
    val product_id: Int? = null, // nullable supaya aman
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val category: String = "",
    val image_url: String = "",
    val created_at: String = "",
    val updated_at: String = ""
) : Serializable

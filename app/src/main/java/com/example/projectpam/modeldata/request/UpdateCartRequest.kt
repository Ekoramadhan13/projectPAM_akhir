package com.example.projectpam.modeldata.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCartRequest(
    val product_id: Int,
    val action: String,
    val amount: Int? = null
)

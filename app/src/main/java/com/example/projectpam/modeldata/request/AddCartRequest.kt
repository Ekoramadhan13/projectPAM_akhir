package com.example.projectpam.modeldata.request

import kotlinx.serialization.Serializable

@Serializable
data class AddCartRequest(
    val product_id: Int,
    val amount: Int
)

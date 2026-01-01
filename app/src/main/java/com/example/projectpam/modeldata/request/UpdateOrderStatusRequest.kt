package com.example.projectpam.modeldata.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateOrderStatusRequest(
    val order_id: Int,
    val status: String
)

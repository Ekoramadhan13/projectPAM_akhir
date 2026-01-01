package com.example.projectpam.modeldata.request

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutRequest(
    val address: String,
    val payment_method: String,   // COD / EWALLET
    val payment_provider: String? // DANA / OVO (nullable)
)

package com.example.projectpam.modeldata.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val role: String,
    val message: String
)


package com.example.projectpam.modeldata.response

import com.example.projectpam.modeldata.Cart
import kotlinx.serialization.Serializable

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

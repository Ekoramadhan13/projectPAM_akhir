package com.example.projectpam.apiservice

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import com.example.projectpam.modeldata.Product
import com.example.projectpam.modeldata.request.LoginRequest
import com.example.projectpam.modeldata.request.RegisterRequest
import com.example.projectpam.modeldata.response.LoginResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ServiceApiEcommerce {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest)

    @GET("api/products")
    suspend fun getAllProducts(): List<Product>

    @Multipart
    @POST("api/products")
    suspend fun addProduct(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("category") category: RequestBody,
        @Part image: MultipartBody.Part?
    ): Product

    @Multipart
    @PUT("api/products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("category") category: RequestBody,
        @Part image: MultipartBody.Part?
    ): Product

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int)

    companion object {
        private const val BASE_URL = "http://10.0.2.2:3000/api/"

        fun create(): ServiceApiEcommerce {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ServiceApiEcommerce::class.java)
        }
    }
}

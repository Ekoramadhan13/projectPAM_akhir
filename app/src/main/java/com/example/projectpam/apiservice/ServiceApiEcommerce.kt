package com.example.projectpam.apiservice

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.example.projectpam.modeldata.Product
import com.example.projectpam.modeldata.Cart
import com.example.projectpam.modeldata.request.LoginRequest
import com.example.projectpam.modeldata.request.RegisterRequest
import com.example.projectpam.modeldata.request.AddCartRequest
import com.example.projectpam.modeldata.response.CartListResponse
import com.example.projectpam.modeldata.response.LoginResponse
import com.example.projectpam.modeldata.response.CartResponse

interface ServiceApiEcommerce {

    // ================= AUTH =================
    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    )

    // ================= ADMIN PRODUCTS =================
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
        @Part image: MultipartBody.Part?,
        @Header("Authorization") token: String
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
        @Part image: MultipartBody.Part?,
        @Header("Authorization") token: String
    ): Product

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    )

    // ================= USER PRODUCTS =================
    @GET("api/user/products")
    suspend fun getUserProducts(): List<Product>

    @GET("api/user/products/{id}")
    suspend fun getUserProductDetail(
        @Path("id") id: Int
    ): Product

    @GET("api/user/products/search")
    suspend fun searchUserProducts(
        @Query("q") query: String,
        @Query("category") category: String?
    ): List<Product>

    // ================= USER CART ================= ✅ FINAL
    @GET("api/user/cart")
    suspend fun getCart(@Header("Authorization") token: String): CartListResponse

    @POST("api/user/cart/add")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body request: AddCartRequest
    ): CartResponse

    @PUT("api/user/cart/update")
    suspend fun updateCart(
        @Header("Authorization") token: String,
        @Query("product_id") productId: Int,
        @Query("action") action: String
    ): CartResponse

    @DELETE("api/user/cart/remove")
    suspend fun removeCart(
        @Header("Authorization") token: String,
        @Query("product_id") productId: Int
    )

    companion object {
        private const val BASE_URL = "http://10.0.2.2:3000/"

        fun create(): ServiceApiEcommerce {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ServiceApiEcommerce::class.java)
        }
    }
}

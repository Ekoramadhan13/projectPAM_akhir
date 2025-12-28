package com.example.projectpam.repositori

import com.example.projectpam.apiservice.ServiceApiEcommerce
import com.example.projectpam.modeldata.Product
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProductRepository(private val api: ServiceApiEcommerce) {

    suspend fun getAllProducts(): List<Product> = api.getAllProducts()

    suspend fun addProductMultipart(
        name: String,
        description: String,
        price: Double,
        stock: Int,
        category: String,
        imageFile: File?
    ): Product {
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryPart = category.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart: MultipartBody.Part? = imageFile?.let {
            val reqFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, reqFile)
        }

        return api.addProduct(namePart, descPart, pricePart, stockPart, categoryPart, imagePart)
    }

    suspend fun updateProductMultipart(
        id: Int,
        name: String,
        description: String,
        price: Double,
        stock: Int,
        category: String,
        imageFile: File?
    ): Product {
        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val descPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val pricePart = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val stockPart = stock.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryPart = category.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart: MultipartBody.Part? = imageFile?.let {
            val reqFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, reqFile)
        }

        return api.updateProduct(id, namePart, descPart, pricePart, stockPart, categoryPart, imagePart)
    }

    suspend fun deleteProduct(id: Int) = api.deleteProduct(id)
}

package com.example.projectpam.repositori

import com.example.projectpam.apiservice.ServiceApiEcommerce
import com.example.projectpam.modeldata.request.LoginRequest
import com.example.projectpam.modeldata.request.RegisterRequest
import com.example.projectpam.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository {

    private val api: ServiceApiEcommerce

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        api = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServiceApiEcommerce::class.java)
    }

    suspend fun login(email: String, password: String) =
        api.login(LoginRequest(email, password))

    suspend fun register(name: String, email: String, password: String) =
        api.register(RegisterRequest(name, email, password))
}

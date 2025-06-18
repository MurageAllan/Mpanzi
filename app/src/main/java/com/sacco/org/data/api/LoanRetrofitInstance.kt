package com.sacco.org.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LoanRetrofitInstance {

    private const val BASE_URL = "https://fac-finance.firstadvantageconsulting.com:8080/"

    private var authToken: String? = null

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")

            // Add Authorization if token is available
            authToken?.let {
                requestBuilder.header("Authorization", "Bearer $it")
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    val api: LoanApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoanApiService::class.java)
    }

    // Function to update the auth token
    fun setAuthToken(token: String) {
        authToken = token
    }

    // Function to clear the auth token (for logout)
    fun clearAuthToken() {
        authToken = null
    }
}
package com.sacco.org.data.api

import com.sacco.org.data.api.model.SignInRequest
import com.sacco.org.data.api.model.SignInResponse
import com.sacco.org.data.api.model.SignUpRequest
import com.sacco.org.data.api.model.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/v1/admin/mobile-users/login")
    suspend fun login(@Body request: SignInRequest): Response<SignInResponse>

    @POST("api/v1/admin/mobile-users/createMobileUser")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>
}
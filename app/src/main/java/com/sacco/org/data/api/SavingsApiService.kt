package com.sacco.org.data.api

import com.sacco.org.data.api.model.SavingsAccountRequest
import com.sacco.org.data.api.model.SavingsAccountResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SavingsApiService {

    @POST("api/v1/admin/mSavingAccount/createSavingAccount")
    suspend fun createSavings(@Body request: SavingsAccountRequest): Response<SavingsAccountResponse>
}
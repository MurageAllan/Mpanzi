package com.sacco.org.data.api

import com.sacco.org.data.api.model.AccountResponse
import com.sacco.org.data.api.model.AccountSummaryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AccountApiService {

    @GET("api/v1/admin/account/savingsAccountMiniStatements/{m_client_id}")
    suspend fun savingsAccountMiniStatements(
        @Path("m_client_id") m_client_id: Int,
    ): Response<AccountResponse>

    @GET("api/v1/admin/account/clientAccountSummary/{m_client_id}")
    suspend fun clientAccountSummary(
        @Path("m_client_id") m_client_id: Int
    ): Response<AccountSummaryResponse>
}
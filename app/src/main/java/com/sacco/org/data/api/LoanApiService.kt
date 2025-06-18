package com.sacco.org.data.api

import com.sacco.org.data.api.model.CreateLoanRequest
import com.sacco.org.data.api.model.LoanApplicationStatusResponse
import com.sacco.org.data.api.model.LoanResponse
import com.sacco.org.data.api.model.RepayLoanRequest
import com.sacco.org.data.api.model.RepayLoanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LoanApiService {

    @POST("api/v1/admin/mLoan/createLoan")
    suspend fun createLoan(@Body request: CreateLoanRequest): Response<LoanResponse>

    @POST("api/v1/admin/mLoanRepaymentSchedule/createLoanRepaymentSchedule")
    suspend fun repayLoan(@Body request: RepayLoanRequest): Response<RepayLoanResponse>

    @GET("api/v1/admin/mLoan/getLoanApplicationStatus/{loan_id}")
    suspend fun getLoanApplicationStatus(
        @Path("loan_id") loan_id: Int
    ): Response<LoanApplicationStatusResponse>
}
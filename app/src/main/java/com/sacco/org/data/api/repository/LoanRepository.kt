package com.sacco.org.data.api.repository

import com.sacco.org.data.api.LoanApiService
import com.sacco.org.data.api.model.CreateLoanRequest
import com.sacco.org.data.api.model.LoanApplicationStatusResponse
import com.sacco.org.data.api.model.LoanResponse
import com.sacco.org.data.api.model.RepayLoanRequest
import com.sacco.org.data.api.model.RepayLoanResponse
import com.sacco.org.data.api.parseErrorMessage
import com.sacco.org.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class LoanRepository(private val apiService: LoanApiService) {

    suspend fun createLoan(request: CreateLoanRequest): Resource<LoanResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.createLoan(request)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Resource.Success(it)
                    } ?: Resource.Error("Empty response")
                } else {
                    Resource.Error(parseErrorMessage(response.errorBody()))
                }
            } catch (e: IOException) {
                Resource.Error("Network error: ${e.localizedMessage}")
            } catch (e: HttpException) {
                Resource.Error("Unexpected error: ${e.localizedMessage}")
            }
        }
    }

    suspend fun repayLoan(request: RepayLoanRequest): Resource<RepayLoanResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.repayLoan(request)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Resource.Success(it)
                    } ?: Resource.Error("Empty response")
                } else {
                    Resource.Error(parseErrorMessage(response.errorBody()))
                }
            } catch (e: IOException) {
                Resource.Error("Network error: ${e.localizedMessage}")
            } catch (e: HttpException) {
                Resource.Error("Unexpected error: ${e.localizedMessage}")
            }
        }
    }

    suspend fun getLoanApplicationStatus(loanId: Int): Resource<LoanApplicationStatusResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getLoanApplicationStatus(loanId)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Resource.Success(it)
                    } ?: Resource.Error("Empty response")
                } else {
                    Resource.Error(parseErrorMessage(response.errorBody()))
                }
            } catch (e: IOException) {
                Resource.Error("Network error: ${e.localizedMessage}")
            } catch (e: HttpException) {
                Resource.Error("Unexpected error: ${e.localizedMessage}")
            }
        }
    }
}
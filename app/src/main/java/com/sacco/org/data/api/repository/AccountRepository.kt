package com.sacco.org.data.api.repository

import com.sacco.org.data.api.AccountApiService
import com.sacco.org.data.api.model.AccountResponse
import com.sacco.org.data.api.model.AccountSummaryResponse
import com.sacco.org.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AccountRepository(
    private val apiService: AccountApiService
) {

    suspend fun getSavingsAccountMiniStatements(clientId: Int): Resource<AccountResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.savingsAccountMiniStatements(clientId)
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

    suspend fun getClientAccountSummary(clientId: Int): Resource<AccountSummaryResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.clientAccountSummary(clientId)
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

    private fun parseErrorMessage(errorBody: okhttp3.ResponseBody?): String {
        return errorBody?.string() ?: "Unknown error"
    }
}
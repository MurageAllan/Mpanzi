package com.sacco.org.data.api.repository

import com.sacco.org.data.api.SavingsApiService
import com.sacco.org.data.api.model.SavingsAccountRequest
import com.sacco.org.data.api.model.SavingsAccountResponse
import com.sacco.org.data.api.parseErrorMessage
import com.sacco.org.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class SavingsRepository(private val apiService: SavingsApiService) {

    suspend fun createSavings(request: SavingsAccountRequest): Resource<SavingsAccountResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.createSavings(request)
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
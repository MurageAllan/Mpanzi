package com.sacco.org.data.api.repository

import com.sacco.org.data.api.AccountRetrofitInstance
import com.sacco.org.data.api.AuthApiService
import com.sacco.org.data.api.model.SignInRequest
import com.sacco.org.data.api.model.SignInResponse
import com.sacco.org.data.api.model.SignUpRequest
import com.sacco.org.data.api.model.SignUpResponse
import com.sacco.org.data.api.parseErrorMessage
import com.sacco.org.data.local.DataStoreManager
import com.sacco.org.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(
    private val apiService: AuthApiService,
    private val dataStoreManager: DataStoreManager
) {

    suspend fun signIn(request: SignInRequest): Resource<SignInResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(request)
                if (response.isSuccessful) {
                    response.body()?.let {
                        val token = it.token
                        // 1. Set token in Retrofit for future API calls
                        AccountRetrofitInstance.setAuthToken(token)
                        // Save token and user data to DataStore
                        dataStoreManager.saveAuthData(it)
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

    suspend fun signUp(request: SignUpRequest): Resource<SignUpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.signUp(request)
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

    // Logout: Clear DataStore and Retrofit token
    suspend fun logout() {
        dataStoreManager.clear()
        AccountRetrofitInstance.clearAuthToken()
    }


}
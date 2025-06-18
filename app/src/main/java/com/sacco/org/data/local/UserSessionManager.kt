package com.sacco.org.data.local

import com.sacco.org.data.api.model.SignInResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class UserSessionManager(private val dataStoreManager: DataStoreManager) {

    // Expose token as flow
    val tokenFlow: Flow<String?> = dataStoreManager.tokenFlow

    // Expose full user details (SignInResponse)
    val signInResponseFlow: Flow<SignInResponse?> = tokenFlow.map { token ->
        token?.let {
            val userId = dataStoreManager.userIdFlow.firstOrNull()
            val name = dataStoreManager.nameFlow.firstOrNull()
            val email = dataStoreManager.emailFlow.firstOrNull()
            val phone = dataStoreManager.phoneFlow.firstOrNull()
            val mclientId = dataStoreManager.clientIdFlow.firstOrNull()
            val expiresAt = dataStoreManager.expiresAtFlow.firstOrNull()

            SignInResponse(
                token = it,
                user_id = userId ?: -1,
                email = email ?: "",
                phone_number = phone ?: "",
                name = name ?: "",
                m_client_id = mclientId ?: -1,
                expires_at = expiresAt ?: 0L
            )
        }
    }.stateIn(
        scope = CoroutineScope(Dispatchers.IO),
        started = SharingStarted.Lazily,
        initialValue = null
    )

    // Save SignInResponse after login
    suspend fun saveSignInResponse(signInResponse: SignInResponse) {
        dataStoreManager.saveAuthData(signInResponse)
    }

    // Clear user data (e.g., on logout)
    suspend fun clearUserData() {
        dataStoreManager.clear()
    }
}

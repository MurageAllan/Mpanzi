package com.sacco.org.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sacco.org.data.api.model.SignInResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("auth_prefs")

class DataStoreManager(private val context: Context) {
    companion object {
        val TOKEN = stringPreferencesKey("token")
        val USER_ID = intPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("name")
        val USER_EMAIL = stringPreferencesKey("email")
        val PHONE = stringPreferencesKey("phone")
        val M_CLIENT_ID = intPreferencesKey("m_client_id")
        val EXPIRES_AT = stringPreferencesKey("expires_at")
    }

    // Save the SignInResponse data
    suspend fun saveAuthData(signInResponse: SignInResponse) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN] = signInResponse.token
            prefs[USER_ID] = signInResponse.user_id
            prefs[USER_NAME] = signInResponse.name
            prefs[USER_EMAIL] = signInResponse.email
            prefs[PHONE] = signInResponse.phone_number
            prefs[M_CLIENT_ID] = signInResponse.m_client_id
            prefs[EXPIRES_AT] = signInResponse.expires_at.toString()
        }
    }

    // Read all auth data into a SignInResponse object
    suspend fun readAuthData(): SignInResponse? {
        val prefs = context.dataStore.data.first()

        val token = prefs[TOKEN] ?: return null
        val userId = prefs[USER_ID] ?: return null
        val name = prefs[USER_NAME] ?: return null
        val email = prefs[USER_EMAIL] ?: return null
        val phone = prefs[PHONE] ?: return null
        val mclientId = prefs[M_CLIENT_ID] ?: return null
        val expiresAtString = prefs[EXPIRES_AT] ?: return null

        // Convert the String (expiresAtString) to Long
        val expiresAt = expiresAtString.toLongOrNull() ?: return null

        return SignInResponse(
            token = token,
            user_id = userId,
            name = name,
            email = email,
            phone_number = phone,
            m_client_id = mclientId,
            expires_at = expiresAt
        )
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { it[TOKEN] }.first()
    }

    // Clear all stored data
    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    // Reactive flow of stored token
    val tokenFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[TOKEN]
    }

    // Reactive flow of stored user id
    val userIdFlow: Flow<Int?> = context.dataStore.data.map { prefs ->
        prefs[USER_ID]
    }

    // Reactive flow of stored user name
    val nameFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_NAME]
    }

    // Reactive flow of stored user email
    val emailFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[USER_EMAIL]
    }

    // Reactive flow of stored phone number
    val phoneFlow: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[PHONE]
    }

    // Reactive flow of stored client id
    val clientIdFlow: Flow<Int?> = context.dataStore.data.map { prefs ->
        prefs[M_CLIENT_ID]
    }

    // Reactive flow of token expiry time
    val expiresAtFlow: Flow<Long?> = context.dataStore.data.map { prefs ->
        prefs[EXPIRES_AT]?.toLongOrNull()
    }
}
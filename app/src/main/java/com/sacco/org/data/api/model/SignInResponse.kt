package com.sacco.org.data.api.model

data class SignInResponse(
    val token: String,
    val expires_at: Long,
    val user_id: Int,
    val email: String,
    val phone_number: String,
    val name: String,
    val m_client_id: Int
)

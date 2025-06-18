package com.sacco.org.data.api.model

data class MobileClient(
    val phone_number: String,
    val pin: String,
    val activation_token: String,
    val full_name: String,
    val email: String,
    val m_client_id: Int,
    val status: Boolean,
    val created: String,
    val modified: String,
    val id: Int
)

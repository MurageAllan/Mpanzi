package com.sacco.org.data.api.model

data class SignInRequest(
    val phone_number: String,
    val pin: String
)

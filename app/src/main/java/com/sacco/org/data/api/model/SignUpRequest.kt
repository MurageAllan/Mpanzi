package com.sacco.org.data.api.model

data class SignUpRequest(
    val phone_number: String,
    val pin: String,
    val confirm_pin: String
)

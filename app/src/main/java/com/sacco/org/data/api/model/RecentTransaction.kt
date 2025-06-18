package com.sacco.org.data.api.model

data class RecentTransaction(
    val transaction_date: String,
    val transaction_type: Int,
    val amount: String
)

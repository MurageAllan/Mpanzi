package com.sacco.org.data.api.model

data class AccountData(
    val account_no: String,
    val account_balance: String,
    val recent_transactions: List<RecentTransaction>
)

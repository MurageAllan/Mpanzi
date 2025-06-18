package com.sacco.org.data.api.model

data class AccountSummary(
    val savingsAccount: SavingsAccount,


    val totalApprovedShares: String?,


    val principalAmount: PrincipalAmount
)

package com.sacco.org.data.api.model

import com.google.gson.annotations.SerializedName

data class RepayLoanRequest(
    @SerializedName("loan_id")
    val loanId: Int,
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("payment_method")
    val paymentMethod: String
)

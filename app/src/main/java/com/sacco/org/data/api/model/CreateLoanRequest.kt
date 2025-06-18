package com.sacco.org.data.api.model

import com.google.gson.annotations.SerializedName

data class CreateLoanRequest(
    @SerializedName("client_id")
    val clientId: Int,

    @SerializedName("product_id")
    val productId: Int = 2,

    @SerializedName("principal_amount")
    val principalAmount: Double,

    @SerializedName("submittedon_date")
    val submittedOnDate: String? = null,

    @SerializedName("loan_type_enum")
    val loanType: Int,

    @SerializedName("repay_every")
    val repaymentFrequency: String
)

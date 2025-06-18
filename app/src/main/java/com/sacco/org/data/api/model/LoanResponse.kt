package com.sacco.org.data.api.model

import com.google.gson.annotations.SerializedName

data class LoanResponse(
    @SerializedName("loan")
    val loan: Loan = Loan()
)

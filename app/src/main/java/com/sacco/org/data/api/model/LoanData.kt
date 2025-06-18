package com.sacco.org.data.api.model

import com.google.gson.annotations.SerializedName

data class LoanData( @SerializedName("loan_amount") val loanAmount: String,
                     @SerializedName("interest_rate") val interestRate: String,
                     @SerializedName("repayment_period") val repaymentPeriod: RepaymentPeriod,
                     @SerializedName("loan_status") val loanStatus: LoanStatus,
                     @SerializedName("status_options") val statusOptions: Map<String, StatusOption>)

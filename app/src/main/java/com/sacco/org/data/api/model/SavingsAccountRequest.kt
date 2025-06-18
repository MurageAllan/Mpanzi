package com.sacco.org.data.api.model

import com.google.gson.annotations.SerializedName

data class SavingsAccountRequest(
    @SerializedName("client_id") val clientId: Int,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("payment_method") val paymentMethod: String,
    @SerializedName("submittedon_date") val submittedOnDate: String
)

package com.sacco.org.data.api.model

import com.google.gson.annotations.SerializedName

data class RepaymentPeriod(
    @SerializedName("repay_every") val repayEvery: Int,
    @SerializedName("frequency_enum") val frequencyEnum: Int
)

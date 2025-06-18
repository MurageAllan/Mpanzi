package com.sacco.org.data.api.model

import com.google.gson.annotations.SerializedName

data class StatusOption(
    @SerializedName("label") val label: String,
    @SerializedName("date") val date: String?
)

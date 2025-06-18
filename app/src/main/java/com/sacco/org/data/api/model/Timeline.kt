package com.sacco.org.data.api.model

data class Timeline(
    val submittedOnDate: List<Int>,
    val activatedOnDate: List<Int>,
    val activatedByUsername: String,
    val activatedByFirstname: String,
    val activatedByLastname: String
)

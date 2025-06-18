package com.sacco.org.data.api

import com.google.gson.Gson
import com.sacco.org.data.api.model.ErrorResponse
import okhttp3.ResponseBody

fun parseErrorMessage(errorBody: ResponseBody?): String {
    return try {
        val errorJson = errorBody?.string()
        val errorResponse = Gson().fromJson(errorJson, ErrorResponse::class.java)
        errorResponse?.message ?: "Something went wrong"
    } catch (e: Exception) {
        "Something went wrong"
    }
}
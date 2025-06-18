package com.sacco.org.util

sealed class Resource<out T> {
    class Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}

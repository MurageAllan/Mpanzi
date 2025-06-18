package com.sacco.org.presentation.viewmodel

sealed class LoanUiState<out T> {
    object Idle : LoanUiState<Nothing>()
    object Loading : LoanUiState<Nothing>()
    data class Success<T>(val data: T) : LoanUiState<T>()
    data class Error(val message: String) : LoanUiState<Nothing>()
}
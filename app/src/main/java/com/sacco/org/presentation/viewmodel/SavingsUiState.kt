package com.sacco.org.presentation.viewmodel

sealed class SavingsUiState<out T> {
    object Idle : SavingsUiState<Nothing>()
    object Loading : SavingsUiState<Nothing>()
    data class Success<T>(val data: T) : SavingsUiState<T>()
    data class Error(val message: String) : SavingsUiState<Nothing>()
}
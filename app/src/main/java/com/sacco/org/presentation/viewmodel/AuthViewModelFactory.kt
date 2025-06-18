package com.sacco.org.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sacco.org.data.api.repository.AuthRepository
import com.sacco.org.data.local.UserSessionManager

class AuthViewModelFactory(
    private val userSessionManager: UserSessionManager, // Pass UserSessionManager here
    private val authRepository: AuthRepository         // Keep AuthRepository as it is
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(userSessionManager, authRepository) as T
    }
}
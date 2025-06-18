package com.sacco.org.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sacco.org.data.api.AuthRetrofitInstance
import com.sacco.org.data.api.model.SignInRequest
import com.sacco.org.data.api.model.SignInResponse
import com.sacco.org.data.api.model.SignUpRequest
import com.sacco.org.data.api.model.SignUpResponse
import com.sacco.org.data.api.repository.AuthRepository
import com.sacco.org.data.local.UserSessionManager
import com.sacco.org.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val userSessionManager: UserSessionManager,
    private val repository: AuthRepository
) : ViewModel() {
    // Sign-in state
    private val _signInState = MutableStateFlow<AuthUiState<SignInResponse>>(AuthUiState.Idle)
    val signInState: StateFlow<AuthUiState<SignInResponse>> = _signInState.asStateFlow()

    // Sign-up state
    private val _signUpState = MutableStateFlow<AuthUiState<SignUpResponse>>(AuthUiState.Idle)
    val signUpState: StateFlow<AuthUiState<SignUpResponse>> = _signUpState.asStateFlow()

    val userFlow = userSessionManager.signInResponseFlow

    fun login(phone: String, pin: String) {
        viewModelScope.launch {
            _signInState.value = AuthUiState.Loading
            when (val result = repository.signIn(SignInRequest(phone, pin))) {
                is Resource.Success -> _signInState.value = AuthUiState.Success(result.data)
                is Resource.Error -> _signInState.value = AuthUiState.Error(result.message)
                else -> Unit
            }
        }
    }

    fun register(phone: String, pin: String, confirmPin: String) {
        viewModelScope.launch {
            _signUpState.value = AuthUiState.Loading
            when (val result = repository.signUp(SignUpRequest(phone, pin, confirmPin))) {
                is Resource.Success -> _signUpState.value = AuthUiState.Success(result.data)
                is Resource.Error -> _signUpState.value = AuthUiState.Error(result.message)
                else -> Unit
            }
        }
    }

    fun resetSignInState() {
        _signInState.value = AuthUiState.Idle
    }

    fun resetSignUpState() {
        _signUpState.value = AuthUiState.Idle
    }

    // Save token and user info after successful login
    fun saveTokenAndUserData(token: String, signInResponse: SignInResponse) {
        viewModelScope.launch {
            userSessionManager.saveSignInResponse(signInResponse)
            AuthRetrofitInstance.setToken(token)
        }
    }
}
package com.sacco.org.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sacco.org.data.api.model.AccountResponse
import com.sacco.org.data.api.model.AccountSummaryResponse
import com.sacco.org.data.api.repository.AccountRepository
import com.sacco.org.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: AccountRepository) : ViewModel() {
    private val _miniStatementsState =
        MutableStateFlow<Resource<AccountResponse>>(Resource.Loading())
    val miniStatementsState: StateFlow<Resource<AccountResponse>> = _miniStatementsState

    private val _accountSummaryState =
        MutableStateFlow<Resource<AccountSummaryResponse>>(Resource.Loading())
    val accountSummaryState: StateFlow<Resource<AccountSummaryResponse>> = _accountSummaryState

    fun fetchSavingsAccountMiniStatements(clientId: Int) {
        viewModelScope.launch {
            _miniStatementsState.value = Resource.Loading()
            try {
                val result = repository.getSavingsAccountMiniStatements(clientId)
                _miniStatementsState.value = result
            } catch (e: Exception) {
                _miniStatementsState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchClientAccountSummary(clientId: Int) {
        viewModelScope.launch {
            _accountSummaryState.value = Resource.Loading()
            try {
                val result = repository.getClientAccountSummary(clientId)
                _accountSummaryState.value = result
            } catch (e: Exception) {
                _accountSummaryState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }
}
package com.sacco.org.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sacco.org.data.api.SavingsRetrofitInstance
import com.sacco.org.data.api.model.SavingsAccountRequest
import com.sacco.org.data.api.model.SavingsAccountResponse
import com.sacco.org.data.api.repository.SavingsRepository
import com.sacco.org.data.local.DataStoreManager
import com.sacco.org.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavingsViewModel(
    private val repository: SavingsRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    init {
        // Set token on init
        viewModelScope.launch {
            dataStoreManager.getToken()?.let {
                SavingsRetrofitInstance.setAuthToken(it)
            }
        }
    }

    // Create Savings state
    private val _createSavingsState =
        MutableStateFlow<SavingsUiState<SavingsAccountResponse>>(SavingsUiState.Idle)
    val createSavingsState: StateFlow<SavingsUiState<SavingsAccountResponse>> =
        _createSavingsState.asStateFlow()

    fun createSavings(
        clientId: Int,
        productId: Int,
        amount: Double,
        paymentMethod: String,
        submittedOnDate: String
    ) {
        viewModelScope.launch {
            _createSavingsState.value = SavingsUiState.Loading
            when (val result = repository.createSavings(
                SavingsAccountRequest(
                    amount = amount,
                    submittedOnDate = submittedOnDate,
                    paymentMethod = paymentMethod,
                    productId = productId,
                    clientId = clientId
                )
            )) {
                is Resource.Success -> _createSavingsState.value =
                    SavingsUiState.Success(result.data)

                is Resource.Error -> _createSavingsState.value =
                    SavingsUiState.Error(result.message)

                else -> Unit
            }
        }
    }
}
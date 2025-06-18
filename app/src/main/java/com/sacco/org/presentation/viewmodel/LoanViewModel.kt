package com.sacco.org.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sacco.org.data.api.LoanRetrofitInstance
import com.sacco.org.data.api.model.CreateLoanRequest
import com.sacco.org.data.api.model.LoanApplicationStatusResponse
import com.sacco.org.data.api.model.LoanResponse
import com.sacco.org.data.api.model.RepayLoanRequest
import com.sacco.org.data.api.model.RepayLoanResponse
import com.sacco.org.data.api.repository.LoanRepository
import com.sacco.org.data.local.DataStoreManager
import com.sacco.org.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LoanViewModel(
    private val repository: LoanRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    init {
        // Set token on init
        viewModelScope.launch {
            dataStoreManager.getToken()?.let {
                LoanRetrofitInstance.setAuthToken(it)
            }
        }
    }

    // Create Loan state
    private val _createLoanState = MutableStateFlow<LoanUiState<LoanResponse>>(LoanUiState.Idle)
    val createLoanState: StateFlow<LoanUiState<LoanResponse>> = _createLoanState.asStateFlow()

    // Create Loan repayment state
    private val _repayLoanState = MutableStateFlow<LoanUiState<RepayLoanResponse>>(LoanUiState.Idle)
    val repayLoanState: StateFlow<LoanUiState<RepayLoanResponse>> = _repayLoanState.asStateFlow()

    // create Loan status state
    private val _loanRepaymentState =
        MutableStateFlow<Resource<LoanApplicationStatusResponse>>(Resource.Loading())
    val loanRepaymentState: StateFlow<Resource<LoanApplicationStatusResponse>> = _loanRepaymentState

    fun createLoan(
        repaymentFrequency: String,
        submittedOnDate: String,
        principalAmount: Double,
        productId: Int = 2,
        clientId: Int,
        loanType: Int
    ) {
        viewModelScope.launch {
            _createLoanState.value = LoanUiState.Loading
            when (val result = repository.createLoan(
                CreateLoanRequest(
                    repaymentFrequency = repaymentFrequency,
                    submittedOnDate = submittedOnDate,
                    principalAmount = principalAmount,
                    productId = productId,
                    clientId = clientId,
                    loanType = loanType
                )
            )) {
                is Resource.Success -> _createLoanState.value = LoanUiState.Success(result.data)
                is Resource.Error -> _createLoanState.value = LoanUiState.Error(result.message)
                else -> Unit
            }
        }
    }

    fun repayLoan(loanId: Int = 22, amount: Double, paymentMethod: String) {
        viewModelScope.launch {
            _repayLoanState.value = LoanUiState.Loading
            when (val result = repository.repayLoan(
                RepayLoanRequest(
                    paymentMethod = paymentMethod,
                    amount = amount,
                    loanId = loanId
                )
            )) {
                is Resource.Success -> _repayLoanState.value = LoanUiState.Success(result.data)
                is Resource.Error -> _repayLoanState.value = LoanUiState.Error(result.message)
                else -> Unit
            }
        }

    }

    fun getLoanApplicationStatus(loanId: Int) {
        viewModelScope.launch {
            _loanRepaymentState.value = Resource.Loading()
            try {
                val result = repository.getLoanApplicationStatus(loanId)
                _loanRepaymentState.value = result
            } catch (e: Exception) {
                _loanRepaymentState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }
}
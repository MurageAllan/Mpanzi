package com.sacco.org.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sacco.org.data.api.repository.LoanRepository
import com.sacco.org.data.local.DataStoreManager

class LoanViewModelFactory(
    private val repository: LoanRepository,
    private val dataStoreManager: DataStoreManager
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoanViewModel(repository, dataStoreManager) as T
    }
}
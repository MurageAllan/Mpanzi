package com.sacco.org.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sacco.org.data.api.repository.SavingsRepository
import com.sacco.org.data.local.DataStoreManager

class SavingsViewModelFactory(
    private val repository: SavingsRepository,
    private val dataStoreManager: DataStoreManager
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SavingsViewModel(repository, dataStoreManager) as T
    }
}
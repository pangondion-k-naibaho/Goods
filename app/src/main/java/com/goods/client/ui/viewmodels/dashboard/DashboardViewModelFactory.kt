package com.goods.client.ui.viewmodels.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goods.client.data.repository.profile.ProfileRepository

class DashboardViewModelFactory(
    private val profileRepository: ProfileRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(profileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
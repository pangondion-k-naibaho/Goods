package com.goods.client.ui.viewmodels.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goods.client.data.repository.asset_by_status.AssetStatusRepository

class HomeViewModelFactory(
    private val assetStatusRepository: AssetStatusRepository,
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(assetStatusRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.goods.client.ui.viewmodels.asset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goods.client.data.repository.all_asset.AllAssetRepository

class AssetViewModelFactory(
    private val allAssetRepository: AllAssetRepository,
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AssetViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AssetViewModel(allAssetRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
package com.goods.client.ui.viewmodels.add_asset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goods.client.data.repository.collection_location.CollectionLocationRepository
import com.goods.client.data.repository.collection_status.CollectionStatusRepository

class AddAssetViewModelFactory(
    private val collectionStatusRepository: CollectionStatusRepository,
    private val collectionLocationRepository: CollectionLocationRepository
):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddAssetViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return AddAssetViewModel(collectionStatusRepository, collectionLocationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
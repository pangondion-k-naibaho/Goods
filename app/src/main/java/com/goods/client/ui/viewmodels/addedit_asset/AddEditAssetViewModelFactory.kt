package com.goods.client.ui.viewmodels.addedit_asset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goods.client.data.repository.collection_location.CollectionLocationRepository
import com.goods.client.data.repository.collection_status.CollectionStatusRepository
import com.goods.client.data.repository.create_asset.CreateAssetRepository
import com.goods.client.data.repository.detail_asset.DetailAssetRepository

class AddEditAssetViewModelFactory(
    private val collectionStatusRepository: CollectionStatusRepository,
    private val collectionLocationRepository: CollectionLocationRepository,
    private val createAssetRepository: CreateAssetRepository,
    private val detailAssetRepository: DetailAssetRepository,
):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddEditAssetViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            val viewModel = AddEditAssetViewModel(collectionStatusRepository,
                collectionLocationRepository, createAssetRepository, detailAssetRepository)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
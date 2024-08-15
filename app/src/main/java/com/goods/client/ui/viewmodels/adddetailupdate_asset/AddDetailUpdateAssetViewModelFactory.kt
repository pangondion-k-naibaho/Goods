package com.goods.client.ui.viewmodels.adddetailupdate_asset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goods.client.data.repository.collection_location.CollectionLocationRepository
import com.goods.client.data.repository.collection_status.CollectionStatusRepository
import com.goods.client.data.repository.create_asset.CreateAssetRepository
import com.goods.client.data.repository.detail_asset.DetailAssetRepository
import com.goods.client.data.repository.update_asset.UpdateAssetRepository

class AddDetailUpdateAssetViewModelFactory(
    private val collectionStatusRepository: CollectionStatusRepository,
    private val collectionLocationRepository: CollectionLocationRepository,
    private val createAssetRepository: CreateAssetRepository,
    private val detailAssetRepository: DetailAssetRepository,
    private val updateAssetRepository: UpdateAssetRepository
):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddDetailUpdateAssetViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            val viewModel = AddDetailUpdateAssetViewModel(collectionStatusRepository,
                collectionLocationRepository, createAssetRepository, detailAssetRepository,
                updateAssetRepository)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
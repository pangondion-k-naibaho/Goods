package com.goods.client.ui.viewmodels.crud_asset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.goods.client.data.repository.collection_location.CollectionLocationRepository
import com.goods.client.data.repository.collection_status.CollectionStatusRepository
import com.goods.client.data.repository.create_asset.CreateAssetRepository
import com.goods.client.data.repository.delete_asset.DeleteAssetRepository
import com.goods.client.data.repository.detail_asset.DetailAssetRepository
import com.goods.client.data.repository.update_asset.UpdateAssetRepository

class CrudAssetViewModelFactory(
    private val collectionStatusRepository: CollectionStatusRepository,
    private val collectionLocationRepository: CollectionLocationRepository,
    private val createAssetRepository: CreateAssetRepository,
    private val detailAssetRepository: DetailAssetRepository,
    private val updateAssetRepository: UpdateAssetRepository,
    private val deleteAssetRepository: DeleteAssetRepository
):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CrudAssetViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            val viewModel = CrudAssetViewModel(collectionStatusRepository,
                collectionLocationRepository, createAssetRepository, detailAssetRepository,
                updateAssetRepository, deleteAssetRepository)
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
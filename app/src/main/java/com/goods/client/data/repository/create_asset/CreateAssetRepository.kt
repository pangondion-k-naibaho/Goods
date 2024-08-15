package com.goods.client.data.repository.create_asset

import com.goods.client.data.model.request.create_update_asset.CreateUpdateAssetRequest
import com.goods.client.data.model.response.create_update_asset.CreateUpdateAssetResponse

interface CreateAssetRepository {
    suspend fun createAsset(tokenAuth: String, createUpdateAssetRequest: CreateUpdateAssetRequest): Result<CreateUpdateAssetResponse>
}
package com.goods.client.data.repository.update_asset

import com.goods.client.data.model.request.create_update_asset.CreateUpdateAssetRequest
import com.goods.client.data.model.response.create_update_asset.CreateUpdateAssetResponse

interface UpdateAssetRepository {
    suspend fun updateAsset(tokenAuth: String, id: String, createUpdateAssetRequest: CreateUpdateAssetRequest): Result<CreateUpdateAssetResponse>
}
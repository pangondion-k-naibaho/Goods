package com.goods.client.data.repository.create_asset

import com.goods.client.data.model.request.asset.CreateAssetRequest
import com.goods.client.data.model.response.create_asset.CreateAssetResponse

interface CreateAssetRepository {
    suspend fun createAsset(tokenAuth: String, createAssetRequest: CreateAssetRequest): Result<CreateAssetResponse>
}
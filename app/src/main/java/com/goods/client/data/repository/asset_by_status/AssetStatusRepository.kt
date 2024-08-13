package com.goods.client.data.repository.asset_by_status

import com.goods.client.data.model.response.asset_by_status.CollectionAssetStatusResponse

interface AssetStatusRepository {
    suspend fun getAssetByStatus(tokenAuth: String): Result<CollectionAssetStatusResponse>
}
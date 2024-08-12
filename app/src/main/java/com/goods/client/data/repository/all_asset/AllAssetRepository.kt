package com.goods.client.data.repository.all_asset

import com.goods.client.data.model.response.all_asset.AllAssetResponse

interface AllAssetRepository {
    suspend fun getAllAsset(tokenAuth: String, page:Int, pageSize: Int?, search: String): Result<AllAssetResponse>
}
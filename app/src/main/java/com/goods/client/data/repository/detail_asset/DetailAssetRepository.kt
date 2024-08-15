package com.goods.client.data.repository.detail_asset

import com.goods.client.data.model.response.detail_asset.DetailAssetResponse

interface DetailAssetRepository {
    suspend fun getDetailAsset(tokenAuth: String, id: String): Result<DetailAssetResponse>
}
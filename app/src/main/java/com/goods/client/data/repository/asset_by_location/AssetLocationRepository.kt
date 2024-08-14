package com.goods.client.data.repository.asset_by_location

import com.goods.client.data.model.response.asset_by_location.CollectionAssetLocationResponse

interface AssetLocationRepository {
    suspend fun getAssetByLocation(tokenAuth: String): Result<CollectionAssetLocationResponse>
}
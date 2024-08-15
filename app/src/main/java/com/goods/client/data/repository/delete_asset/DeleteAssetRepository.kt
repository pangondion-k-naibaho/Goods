package com.goods.client.data.repository.delete_asset

interface DeleteAssetRepository {
    suspend fun deleteAsset(tokenAuth: String, id: String): Result<Unit>
}
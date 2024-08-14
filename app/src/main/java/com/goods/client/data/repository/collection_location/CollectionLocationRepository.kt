package com.goods.client.data.repository.collection_location

import com.goods.client.data.model.response.collection_location.CollectionLocationResponse

interface CollectionLocationRepository {
    suspend fun getCollectionLocation(tokenAuth: String): Result<CollectionLocationResponse>
}
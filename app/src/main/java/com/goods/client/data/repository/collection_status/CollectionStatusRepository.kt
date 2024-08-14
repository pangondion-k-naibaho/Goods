package com.goods.client.data.repository.collection_status

import com.goods.client.data.model.response.collection_status.CollectionStatusResponse

interface CollectionStatusRepository {
    suspend fun getCollectionStatus(tokenAuth: String): Result<CollectionStatusResponse>
}
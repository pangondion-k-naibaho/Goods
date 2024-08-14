package com.goods.client.data.repository.collection_location

import com.goods.client.data.model.response.collection_location.CollectionLocationResponse
import com.goods.client.data.remote.ApiService

class CollectionLocationRepositoryImpl(private val apiService: ApiService): CollectionLocationRepository {
    override suspend fun getCollectionLocation(tokenAuth: String): Result<CollectionLocationResponse> {
        return try {
            val response = apiService.getCollectionLocation(tokenAuth)
            if(response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}
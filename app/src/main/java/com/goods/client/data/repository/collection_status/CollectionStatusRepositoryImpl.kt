package com.goods.client.data.repository.collection_status

import com.goods.client.data.model.response.collection_status.CollectionStatusResponse
import com.goods.client.data.remote.ApiService

class CollectionStatusRepositoryImpl(private val apiService: ApiService): CollectionStatusRepository{
    override suspend fun getCollectionStatus(tokenAuth: String): Result<CollectionStatusResponse> {
        return try{
            val response = apiService.getCollectionStatus(tokenAuth)
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
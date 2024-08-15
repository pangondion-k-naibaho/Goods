package com.goods.client.data.repository.delete_asset

import com.goods.client.data.remote.ApiService

class DeleteAssetRepositoryImpl(private val apiService: ApiService):DeleteAssetRepository {
    override suspend fun deleteAsset(tokenAuth: String, id: String): Result<Unit> {
        return try {
            val response = apiService.deleteAsset(tokenAuth, id)
            when{
                response.isSuccessful || response.code() == 204 -> Result.success(Unit)
                else -> Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}
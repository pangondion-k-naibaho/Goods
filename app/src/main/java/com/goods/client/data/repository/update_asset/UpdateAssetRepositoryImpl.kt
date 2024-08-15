package com.goods.client.data.repository.update_asset

import com.goods.client.data.model.request.create_update_asset.CreateUpdateAssetRequest
import com.goods.client.data.model.response.create_update_asset.CreateUpdateAssetResponse
import com.goods.client.data.remote.ApiService

class UpdateAssetRepositoryImpl(private val apiService: ApiService): UpdateAssetRepository {
    override suspend fun updateAsset(
        tokenAuth: String,
        id: String,
        createUpdateAssetRequest: CreateUpdateAssetRequest
    ): Result<CreateUpdateAssetResponse> {
        return try {
            val response = apiService.updateAsset(tokenAuth, id, createUpdateAssetRequest)
            if(response.isSuccessful || response.code() == 201){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}
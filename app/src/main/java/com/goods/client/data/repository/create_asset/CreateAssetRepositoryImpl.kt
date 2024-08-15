package com.goods.client.data.repository.create_asset

import com.goods.client.data.model.request.asset.CreateAssetRequest
import com.goods.client.data.model.response.create_asset.CreateAssetResponse
import com.goods.client.data.remote.ApiService

class CreateAssetRepositoryImpl(private val apiService: ApiService): CreateAssetRepository {
    override suspend fun createAsset(tokenAuth: String, createAssetRequest: CreateAssetRequest): Result<CreateAssetResponse>{
        return try {
            val response = apiService.createAsset2(tokenAuth, createAssetRequest)
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
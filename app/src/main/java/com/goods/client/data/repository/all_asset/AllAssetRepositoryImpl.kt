package com.goods.client.data.repository.all_asset

import com.goods.client.data.model.response.all_asset.AllAssetResponse
import com.goods.client.data.remote.ApiService

class AllAssetRepositoryImpl(private val apiService: ApiService): AllAssetRepository {
    override suspend fun getAllAsset(
        tokenAuth: String,
        page: Int,
        pageSize: Int?,
        search: String
    ): Result<AllAssetResponse> {
        return try{
            val response = apiService.getAllAsset(tokenAuth, page, pageSize, search)
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
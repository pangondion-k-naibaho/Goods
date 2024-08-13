package com.goods.client.data.repository.asset_by_status

import com.goods.client.data.model.response.asset_by_status.CollectionAssetStatusResponse
import com.goods.client.data.remote.ApiService

class AssetStatusRepositoryImpl(private val apiService: ApiService):AssetStatusRepository {
    override suspend fun getAssetByStatus(tokenAuth: String): Result<CollectionAssetStatusResponse> {
        return try{
            val response = apiService.getAssetByStatus(tokenAuth)
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
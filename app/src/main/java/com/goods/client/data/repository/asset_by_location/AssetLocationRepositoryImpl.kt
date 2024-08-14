package com.goods.client.data.repository.asset_by_location

import com.goods.client.data.model.response.asset_by_location.CollectionAssetLocationResponse
import com.goods.client.data.remote.ApiService

class AssetLocationRepositoryImpl(private val apiService: ApiService): AssetLocationRepository{
    override suspend fun getAssetByLocation(tokenAuth: String): Result<CollectionAssetLocationResponse> {
        return try {
            val response = apiService.getAssetByLocation(tokenAuth)
            if(response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
    }


}
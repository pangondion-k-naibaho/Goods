package com.goods.client.data.repository.detail_asset

import com.goods.client.data.model.response.detail_asset.DetailAssetResponse
import com.goods.client.data.remote.ApiService

class DetailAssetRepositoryImpl(private val apiService: ApiService): DetailAssetRepository{
    override suspend fun getDetailAsset(
        tokenAuth: String,
        id: String
    ): Result<DetailAssetResponse> {
        return try{
            val response = apiService.getDetailAsset(tokenAuth, id)
            if(response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Error ${response.code()} : ${response.message()}"))
            }
        }catch (e:Exception){
            Result.failure(e)
        }
    }

}
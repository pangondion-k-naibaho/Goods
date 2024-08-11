package com.goods.client.data.repository.profile

import com.goods.client.data.model.response.profile.ProfileResponse
import com.goods.client.data.remote.ApiService

class ProfileRepositoryImpl(private val apiService: ApiService):ProfileRepository {
    override suspend fun getProfile(tokenAuth: String): Result<ProfileResponse> {
        return try {
            val response = apiService.getProfile(tokenAuth)
            if (response.isSuccessful){
                Result.success(response.body()!!)
            }else{
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}
package com.goods.client.data.repository.logout

import com.goods.client.data.model.response.logout.LogoutResponse
import com.goods.client.data.remote.ApiService

class LogoutRepositoryImpl(private val apiService: ApiService):LogoutRepository{
    override suspend fun logoutUser(tokenAuth: String): Result<LogoutResponse> {
        return try{
            val response = apiService.logoutUser(tokenAuth)
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
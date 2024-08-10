package com.goods.client.data.remote

import com.goods.client.data.model.request.LoginRequest
import com.goods.client.data.model.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): LoginResponse
}
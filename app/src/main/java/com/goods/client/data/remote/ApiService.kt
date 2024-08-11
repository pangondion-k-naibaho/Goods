package com.goods.client.data.remote

import com.goods.client.data.model.request.login.LoginRequest
import com.goods.client.data.model.request.token.TokenRequest
import com.goods.client.data.model.response.login.LoginResponse
import com.goods.client.data.model.response.logout.LogoutResponse
import com.goods.client.data.model.response.profile.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>


    @GET("auth/me")
    suspend fun getProfile(
        @Header("Authorization") authToken: String
    ): Response<ProfileResponse>

    @POST("auth/token")
    suspend fun generateToken(
        @Body tokenRequest: TokenRequest
    ):Response<String>

    @POST("auth/logout")
    suspend fun logoutUser(
        @Header("Authorization") authToken: String
    ):Response<LogoutResponse>
}
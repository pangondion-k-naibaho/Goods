package com.goods.client.data.remote

import com.goods.client.data.model.request.login.LoginRequest
import com.goods.client.data.model.request.token.TokenRequest
import com.goods.client.data.model.response.all_asset.AllAssetResponse
import com.goods.client.data.model.response.asset_by_location.CollectionAssetLocationResponse
import com.goods.client.data.model.response.asset_by_status.CollectionAssetStatusResponse
import com.goods.client.data.model.response.collection_location.CollectionLocationResponse
import com.goods.client.data.model.response.collection_status.CollectionStatusResponse
import com.goods.client.data.model.response.login.LoginResponse
import com.goods.client.data.model.response.logout.LogoutResponse
import com.goods.client.data.model.response.profile.ProfileResponse
import com.goods.client.data.repository.collection_status.CollectionStatusRepository
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

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

    @GET("asset/")
    suspend fun getAllAsset(
        @Header("Authorization") authToken: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize:Int?=10,
        @Query("search") search: String,
    ): Response<AllAssetResponse>

    @GET("home/agg-asset-by-status/")

    suspend fun getAssetByStatus(
        @Header("Authorization") authToken: String,
    ): Response<CollectionAssetStatusResponse>

    @GET("home/agg-asset-by-location/")
    suspend fun getAssetByLocation(
        @Header("Authorization") authToken: String,
    ): Response<CollectionAssetLocationResponse>

    @GET("status/")
    suspend fun getCollectionStatus(
        @Header("Authorization") authToken: String
    ): Response<CollectionStatusResponse>

    @GET("location/")
    suspend fun getCollectionLocation(
        @Header("Authorization") authToken: String
    ): Response<CollectionLocationResponse>
}
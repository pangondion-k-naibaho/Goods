package com.goods.client.data.repository.login

import com.goods.client.data.model.request.login.LoginRequest
import com.goods.client.data.model.response.login.LoginResponse
import com.goods.client.data.remote.ApiService

class LoginRepositoryImpl(private val apiService: ApiService):LoginRepository {
    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(email, password)
            val response = apiService.loginUser(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
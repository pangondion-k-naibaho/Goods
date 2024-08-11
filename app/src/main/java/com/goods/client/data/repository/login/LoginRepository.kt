package com.goods.client.data.repository.login

import com.goods.client.data.model.response.login.LoginResponse

//interface LoginRepository {
//
//}
interface LoginRepository {
    suspend fun login(email: String, password: String): Result<LoginResponse>
}
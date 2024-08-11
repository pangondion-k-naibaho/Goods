package com.goods.client.data.repository.logout

import com.goods.client.data.model.response.logout.LogoutResponse

interface LogoutRepository {
    suspend fun logoutUser(tokenAuth: String):Result<LogoutResponse>
}
package com.goods.client.data.repository.profile

import com.goods.client.data.model.response.profile.ProfileResponse

interface ProfileRepository {
    suspend fun getProfile(tokenAuth: String): Result<ProfileResponse>
}
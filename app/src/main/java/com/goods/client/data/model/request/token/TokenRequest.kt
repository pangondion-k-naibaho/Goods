package com.goods.client.data.model.request.token

import com.goods.client.data.Constants.GRANT_TYPE_PATTERN.Companion.PASSWORD

data class TokenRequest(
    val grant_type: String?= PASSWORD,
    val username: String,
    val password: String,
    val scope: String?="",
    val client_id: String?="",
    val client_secret: String?=""
)
package com.goods.client.data.model.request.asset

data class CreateAssetRequest(
    val name: String,
    val status_id: String,
    val location_id: String,
)
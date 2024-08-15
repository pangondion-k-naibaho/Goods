package com.goods.client.data.model.request.create_update_asset

data class CreateUpdateAssetRequest(
    val name: String,
    val status_id: String,
    val location_id: String,
)
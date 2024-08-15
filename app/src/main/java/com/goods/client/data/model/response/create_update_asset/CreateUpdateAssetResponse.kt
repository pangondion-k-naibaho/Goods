package com.goods.client.data.model.response.create_update_asset

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateUpdateAssetResponse(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("location_id") val locationId: String,
    @field:SerializedName("status_id") val statusId: String,
):Parcelable
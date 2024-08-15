package com.goods.client.data.model.response.detail_asset

import android.os.Parcelable
import com.goods.client.data.model.response.asset_by_location.LocationResponse
import com.goods.client.data.model.response.asset_by_status.StatusResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailAssetResponse(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("status") val status: StatusResponse,
    @field:SerializedName("location") val location: LocationResponse
):Parcelable
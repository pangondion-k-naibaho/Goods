package com.goods.client.data.model.response.asset_by_location

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemAssetLocationResponse(
    @field:SerializedName("location") val location: LocationResponse,
    @field:SerializedName("count") val count: Int,
):Parcelable
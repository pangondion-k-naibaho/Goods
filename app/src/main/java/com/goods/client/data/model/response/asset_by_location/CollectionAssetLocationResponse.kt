package com.goods.client.data.model.response.asset_by_location

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CollectionAssetLocationResponse(
    @field:SerializedName("results") val results: List<ItemAssetLocationResponse>,
):Parcelable
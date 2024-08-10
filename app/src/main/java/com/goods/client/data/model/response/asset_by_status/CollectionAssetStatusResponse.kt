package com.goods.client.data.model.response.asset_by_status

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CollectionAssetStatusResponse(
    @field:SerializedName("results") val results: List<ItemAssetStatusResponse>,
):Parcelable
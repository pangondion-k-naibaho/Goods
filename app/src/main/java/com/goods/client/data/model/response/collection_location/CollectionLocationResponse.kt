package com.goods.client.data.model.response.collection_location

import android.os.Parcelable
import com.goods.client.data.model.response.asset_by_location.LocationResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CollectionLocationResponse(
    @field:SerializedName("results") val results:List<LocationResponse>,
):Parcelable
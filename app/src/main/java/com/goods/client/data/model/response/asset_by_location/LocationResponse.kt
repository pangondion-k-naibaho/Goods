package com.goods.client.data.model.response.asset_by_location

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationResponse(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String,
):Parcelable
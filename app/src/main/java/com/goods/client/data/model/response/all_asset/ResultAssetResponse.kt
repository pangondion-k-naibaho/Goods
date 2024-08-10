package com.goods.client.data.model.response.all_asset

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultAssetResponse(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String,
):Parcelable
package com.goods.client.data.model.response.all_asset

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AllAssetResponse(
    @field:SerializedName("counts") val counts: Int,
    @field:SerializedName("page_count") val pageCount: Int,
    @field:SerializedName("page_size") val pageSize: Int,
    @field:SerializedName("page") val page: Int,
    @field:SerializedName("results") val results: List<ResultAssetResponse>,
):Parcelable
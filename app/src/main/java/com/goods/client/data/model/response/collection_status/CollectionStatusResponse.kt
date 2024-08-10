package com.goods.client.data.model.response.collection_status

import android.os.Parcelable
import com.goods.client.data.model.response.asset_by_status.StatusResponse
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CollectionStatusResponse(
    @field:SerializedName("results") val results: List<StatusResponse>,
):Parcelable
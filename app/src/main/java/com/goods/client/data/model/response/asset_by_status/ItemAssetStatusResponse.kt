package com.goods.client.data.model.response.asset_by_status

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemAssetStatusResponse(
    @field:SerializedName("status") val status: StatusResponse,
    @field:SerializedName("count") val count: Int,
):Parcelable
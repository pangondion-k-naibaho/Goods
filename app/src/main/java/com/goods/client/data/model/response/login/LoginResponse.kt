package com.goods.client.data.model.response.login

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    @field: SerializedName("id") val id: String,
    @field:SerializedName("email") val email: String,
    @field:SerializedName("username") val username: String,
    @field:SerializedName("is_active") val isActive: Boolean,
    @field:SerializedName("token") val token: String,
):Parcelable
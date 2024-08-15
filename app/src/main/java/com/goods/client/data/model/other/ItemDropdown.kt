package com.goods.client.data.model.other

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemDropdown(
    var id: String?= "",
    var name: String?= "",
    var isSelected: Boolean = false
): Parcelable
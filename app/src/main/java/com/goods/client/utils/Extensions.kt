package com.goods.client.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import com.goods.client.data.model.other.ItemDropdown
import com.goods.client.data.model.response.asset_by_location.CollectionAssetLocationResponse
import com.goods.client.data.model.response.asset_by_location.LocationResponse
import com.goods.client.data.model.response.asset_by_status.CollectionAssetStatusResponse
import com.goods.client.data.model.response.asset_by_status.StatusResponse
import java.text.SimpleDateFormat
import java.util.Locale

class Extensions {
    companion object{
        fun Int.dpToPx(context: Context): Int {
            val scale = context.resources.displayMetrics.density
            return (this * scale + 0.5f).toInt()
        }
        fun String.formatDate(): String {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
            val date = inputFormat.parse(this)
            return outputFormat.format(date!!)
        }

        fun CollectionAssetStatusResponse.retrieveValuebasedOnStatus(inputStatus: String): Int {
            return results.find { it.status.name == inputStatus }?.count ?: 0
        }

        fun CollectionAssetLocationResponse.retrieveValueBasedOnLocation(inputLocation: String): Int{
            return results.find { it.location.name == inputLocation }?.count ?: 0
        }

        fun Activity.getHalfScreenWidthInFloat(): Float {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            // Mendapatkan lebar layar dan membaginya menjadi 50%
            return (displayMetrics.widthPixels / 2).toFloat()
        }

        fun retrieveListItemDropdownLocation(input: List<LocationResponse>): List<ItemDropdown>{
            val arrListItemDropdown = ArrayList<ItemDropdown>()

            for(i in input.indices){
                val itemDropdown = ItemDropdown(
                    id = input[i].id,
                    name = input[i].name
                )
                arrListItemDropdown.add(itemDropdown)
            }

            return arrListItemDropdown
        }

        fun retrieveListItemDropdownStatus(input: List<StatusResponse>): List<ItemDropdown>{
            val arrListItemDropdown = ArrayList<ItemDropdown>()

            for(i in input.indices){
                val itemDropdown = ItemDropdown(
                    id = input[i].id,
                    name = input[i].name
                )
                arrListItemDropdown.add(itemDropdown)
            }

            return arrListItemDropdown
        }
    }
}
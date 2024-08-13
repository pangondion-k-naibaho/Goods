package com.goods.client.utils

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.goods.client.data.model.response.asset_by_status.CollectionAssetStatusResponse
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

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

//        fun CollectionAssetStatusResponse.retrieveValuebasedOnStatus(inputStatus: String): String {
//            return results.find { it.status.name == inputStatus }?.count?.toString() ?: "0"
//        }

        fun CollectionAssetStatusResponse.retrieveValuebasedOnStatus(inputStatus: String): Int {
            return results.find { it.status.name == inputStatus }?.count ?: 0
        }
    }
}
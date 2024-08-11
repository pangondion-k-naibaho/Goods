package com.goods.client.ui.activities.main

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.goods.client.R
import com.goods.client.data.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.goods.client.data.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.goods.client.ui.activities.dashboard.DashboardActivity
import com.goods.client.ui.activities.login.LoginActivity

class MainActivity : ComponentActivity() {
    private val TAG = MainActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appPreferences = this@MainActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        val retrievedToken = appPreferences.getString(TOKEN_KEY, null)

        if(retrievedToken.isNullOrEmpty()){
            startActivity(LoginActivity.newIntent(this@MainActivity))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }else{
            startActivity(DashboardActivity.newIntent(this@MainActivity))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }
}
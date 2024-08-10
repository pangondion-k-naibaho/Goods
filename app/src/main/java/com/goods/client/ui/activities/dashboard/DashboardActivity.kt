package com.goods.client.ui.activities.dashboard

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.goods.client.R
import com.goods.client.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {
    private val TAG = DashboardActivity::class.java.simpleName
    private lateinit var binding: ActivityDashboardBinding

    companion object{
        fun newIntent(context: Context): Intent = Intent(context, DashboardActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
    }
}
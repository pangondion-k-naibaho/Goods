package com.goods.client.ui.activities.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.goods.client.R
import com.goods.client.data.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.goods.client.data.Constants.PREFERENCES.Companion.EMAIL_KEY
import com.goods.client.data.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.goods.client.data.Constants.PREFERENCES.Companion.USERNAME_KEY
import com.goods.client.databinding.ActivityDashboardBinding
import com.goods.client.ui.activities.dashboard.fragments.AssetFragment
import com.goods.client.ui.activities.dashboard.fragments.HomeFragment
import com.goods.client.ui.custom_components.HomeActionBar
import com.google.android.material.navigation.NavigationBarView

class DashboardActivity : AppCompatActivity(), FragmentsDashboardCommunicator{
    private val TAG = DashboardActivity::class.java.simpleName
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var userToken: String? = null
    private var userName: String? = null
    private var userEmail: String? = null

    private var previousIdMenu: Int? = null
    private lateinit var adapterFragmentDashboard: AdapterFragmentDashboard

    companion object{
        fun newIntent(context: Context): Intent = Intent(context, DashboardActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        sharedPreferences = this@DashboardActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        userToken = sharedPreferences.getString(TOKEN_KEY, "adsadadsad")
        userEmail = sharedPreferences.getString(EMAIL_KEY, null)
        userName = sharedPreferences.getString(USERNAME_KEY, null)

        binding.habDashboard.apply {
            setUsername(userName!!)
            setEmail(userEmail!!)
            setListener(object: HomeActionBar.HomeActionbarListener{
                override fun onLogoutClicked() {
                    Log.d(TAG, "Logout")
                }
            })
        }

        binding.vpDashboard.apply {
            adapterFragmentDashboard = AdapterFragmentDashboard(
                supportFragmentManager,
                lifecycle
            )

            val homeFragment = HomeFragment.newInstance(userToken!!)
            val assetFragment = AssetFragment.newInstance(userToken!!)

            val listFragment = listOf(homeFragment, assetFragment)

            adapterFragmentDashboard.fragmentList.addAll(listFragment)

            offscreenPageLimit = listFragment.size
            adapter = adapterFragmentDashboard
            currentItem = 0
            registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    when(position){
                        0 ->{
                            binding.bnvDashboard.selectedItemId = R.id.menu_home
                        }
                        1->{
                            binding.bnvDashboard.selectedItemId = R.id.menu_asset
                        }
                    }
                }
            })

        }

        binding.bnvDashboard.apply {
            selectedItemId = R.id.menu_home
            previousIdMenu = R.id.menu_home
            setOnItemSelectedListener(object: NavigationBarView.OnItemSelectedListener{
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    when(item.itemId){
                        R.id.menu_home ->{
                            Log.d(TAG, "menu_home")
                            previousIdMenu = R.id.menu_home
                            binding.vpDashboard.setCurrentItem(0, false)
                        }
                        R.id.menu_asset ->{
                            Log.d(TAG, "menu_asset")
                            previousIdMenu = R.id.menu_asset
                            binding.vpDashboard.setCurrentItem(1, false)
                        }
                    }
                    return true
                }

            })
        }
    }

    override fun startLoading() {
        binding.loadingLayout.visibility = View.VISIBLE
    }

    override fun stopLoading() {
        binding.loadingLayout.visibility = View.GONE
    }
}
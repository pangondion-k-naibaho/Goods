package com.goods.client.ui.activities.dashboard

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.goods.client.R
import com.goods.client.data.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.goods.client.data.Constants.PREFERENCES.Companion.EMAIL_KEY
import com.goods.client.data.Constants.PREFERENCES.Companion.PASSWORD_KEY
import com.goods.client.data.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.goods.client.data.Constants.PREFERENCES.Companion.USERNAME_KEY
import com.goods.client.data.remote.ApiConfig
import com.goods.client.data.repository.logout.LogoutRepositoryImpl
import com.goods.client.data.repository.profile.ProfileRepositoryImpl
import com.goods.client.databinding.ActivityDashboardBinding
import com.goods.client.ui.activities.add_asset.AddAssetActivity
import com.goods.client.ui.activities.dashboard.fragments.AssetFragment
import com.goods.client.ui.activities.dashboard.fragments.HomeFragment
import com.goods.client.ui.activities.login.LoginActivity
import com.goods.client.ui.custom_components.HomeActionBar
import com.goods.client.ui.custom_components.PopUpNotificationListener
import com.goods.client.ui.custom_components.showPopUpNitification
import com.goods.client.ui.viewmodels.dashboard.DashboardViewModel
import com.goods.client.ui.viewmodels.dashboard.DashboardViewModelFactory
import com.google.android.material.navigation.NavigationBarView

class DashboardActivity : AppCompatActivity(), FragmentsDashboardCommunicator{
    private val TAG = DashboardActivity::class.java.simpleName
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var userToken: String? = null
    private var userName: String?= null
    private var userEmail: String?= null

    private var previousIdMenu: Int? = null
    private lateinit var adapterFragmentDashboard: AdapterFragmentDashboard
    private lateinit var dashboardViewModel: DashboardViewModel

    companion object{
        fun newIntent(context: Context): Intent = Intent(context, DashboardActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = ApiConfig.createApiService()
        val profileRepository = ProfileRepositoryImpl(apiService)
        val logoutRepository = LogoutRepositoryImpl(apiService)

        val factory = DashboardViewModelFactory(profileRepository, logoutRepository)
        dashboardViewModel = ViewModelProvider(this, factory)[DashboardViewModel::class.java]

        observeStatus()

        initView()
    }

    private fun observeStatus(){
        dashboardViewModel.isLoading.observe(this@DashboardActivity, {
            if(it) this@DashboardActivity.startLoading() else this@DashboardActivity.stopLoading()
        })

        dashboardViewModel.isFail.observe(this@DashboardActivity, {
            Toast.makeText(this@DashboardActivity, "Failed to retrieve profile", Toast.LENGTH_SHORT).show()
        })

        dashboardViewModel.isLogoutFail.observe(this@DashboardActivity, {
            this@DashboardActivity.showPopUpNitification(
                textTitle = getString(R.string.popupLogoutFailedTitle),
                textDesc = getString(R.string.popupLogoutFailedDesc),
                backgroundImage = R.drawable.ic_fail,
            )
        })
    }

    private fun initView(){
        sharedPreferences = this@DashboardActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        userToken = sharedPreferences.getString(TOKEN_KEY, "Unknown")

        Log.d(TAG, "userToken: $userToken")

        dashboardViewModel.getProfile(userToken!!)

        dashboardViewModel.profileResponse.observe(this@DashboardActivity, {response->
//            val appPreferences = this@DashboardActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(TOKEN_KEY, response.refreshedToken)
            editor.apply()

            binding.habDashboard.apply {
                setUsername(response.username)
                setEmail(response.email)
                setListener(object: HomeActionBar.HomeActionbarListener{
                    override fun onLogoutClicked() {
                        Log.d(TAG, "Logout")
                        dashboardViewModel.logoutUser(sharedPreferences.getString(TOKEN_KEY, "unknown")!!)

                        dashboardViewModel.logoutResponse.observe(this@DashboardActivity, {response->
                            Log.d(TAG, "logout response:$response")
                            if(response!=null){
                                editor.remove(TOKEN_KEY)
                                editor.remove(EMAIL_KEY)
                                editor.remove(USERNAME_KEY)
                                editor.remove(PASSWORD_KEY)
                                editor.apply()

                                if(editor.commit()){
                                    this@DashboardActivity.showPopUpNitification(
                                        textTitle = getString(R.string.popupLogoutSuccessTitle),
                                        textDesc = getString(R.string.popupLogoutSuccessDesc),
                                        backgroundImage = R.drawable.ic_success,
                                        listener = object: PopUpNotificationListener{
                                            override fun onPopUpClosed() {
                                                startActivity(LoginActivity.newIntent(this@DashboardActivity))
                                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                                finish()
                                            }
                                        }
                                    )
                                }
                            }
//                            else{
//                                this@DashboardActivity.showPopUpNitification(
//                                    textTitle = getString(R.string.popupLogoutFailedTitle),
//                                    textDesc = getString(R.string.popupLogoutFailedDesc),
//                                    backgroundImage = R.drawable.ic_fail,
//                                )
//                            }
                        })
                    }
                })
            }
        })

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

        binding.btnInputAsset.setOnClickListener {
            startActivity(AddAssetActivity.newIntent(this@DashboardActivity))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun startLoading() {
        binding.loadingLayout.visibility = View.VISIBLE
    }

    override fun stopLoading() {
        binding.loadingLayout.visibility = View.GONE
    }

    override fun updateProfile(username: String, email: String) {
        binding.habDashboard.apply {
            setUsername(username)
            setEmail(email)
        }
    }
}
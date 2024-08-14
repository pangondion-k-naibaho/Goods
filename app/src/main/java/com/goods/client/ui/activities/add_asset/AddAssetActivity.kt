package com.goods.client.ui.activities.add_asset

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.goods.client.R
import com.goods.client.data.Constants
import com.goods.client.data.model.other.ItemDropdown
import com.goods.client.data.remote.ApiConfig
import com.goods.client.data.repository.collection_location.CollectionLocationRepositoryImpl
import com.goods.client.data.repository.collection_status.CollectionStatusRepositoryImpl
import com.goods.client.databinding.ActivityAddAssetBinding
import com.goods.client.ui.activities.login.LoginActivity
import com.goods.client.ui.custom_components.InputDropdownView
import com.goods.client.ui.custom_components.PopUpNotificationListener
import com.goods.client.ui.custom_components.showPopUpNitification
import com.goods.client.ui.viewmodels.add_asset.AddAssetViewModel
import com.goods.client.ui.viewmodels.add_asset.AddAssetViewModelFactory
import com.goods.client.utils.Extensions.Companion.retrieveListItemDropdownLocation
import com.goods.client.utils.Extensions.Companion.retrieveListItemDropdownStatus

class AddAssetActivity : AppCompatActivity() {
    private val TAG = AddAssetActivity::class.java.simpleName
    private lateinit var binding: ActivityAddAssetBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var addAssetViewModel: AddAssetViewModel
    private var userToken = ""

    private var selectedStatus: String?= null
    private var selectedLocation: String?= null

    companion object{
        fun newIntent(context: Context): Intent = Intent(context, AddAssetActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAssetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = ApiConfig.createApiService()
        val collectionStatusRepository = CollectionStatusRepositoryImpl(apiService)
        val collectionLocationRepository = CollectionLocationRepositoryImpl(apiService)

        val factory = AddAssetViewModelFactory(collectionStatusRepository, collectionLocationRepository)
        addAssetViewModel = ViewModelProvider(this@AddAssetActivity, factory)[AddAssetViewModel::class.java]

        observeStatus()
        initView()
    }

    private fun observeStatus(){
        addAssetViewModel.isLoading.observe(this@AddAssetActivity, {
            setLayoutForLoading(it)
        })

        addAssetViewModel.isFail.observe(this@AddAssetActivity, {
            Toast.makeText(this@AddAssetActivity, "Unable to retrieve data...", Toast.LENGTH_SHORT).show()
        })

        addAssetViewModel.isUnauthorized.observe(this@AddAssetActivity, {
            if(it){
                showPopUpNitification(
                    textTitle = getString(R.string.popupUnauthorizedTitle),
                    textDesc = getString(R.string.popupUnauthorizedDesc),
                    backgroundImage = R.drawable.ic_fail,
                    listener = object: PopUpNotificationListener{
                        override fun onPopUpClosed() {
                            startActivity(LoginActivity.newIntent(this@AddAssetActivity))
                            this@AddAssetActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            this@AddAssetActivity.finish()
                        }
                    }
                )
            }
        })
    }

    private fun initView(){
        sharedPreferences = this@AddAssetActivity.getSharedPreferences(Constants.PREFERENCES.APP_PREFERENCES, Context.MODE_PRIVATE)

        userToken = sharedPreferences.getString(Constants.PREFERENCES.TOKEN_KEY, "")!!

        addAssetViewModel.getStatusCollection(userToken)
        addAssetViewModel.getLocationCollection(userToken)

        addAssetViewModel.collectionStatusResponse.observe(this@AddAssetActivity, {response->
            Log.d(TAG, "collection Status Response: $response")
            val collectionItemDropdown = retrieveListItemDropdownStatus(response.results)

            binding.idvStatusAsset.apply {
                setHint(getString(R.string.dropdownHintStatus))
                setData(collectionItemDropdown)
                setListener(object: InputDropdownView.DropdownListener{
                    override fun onItemSelected(
                        position: Int,
                        item: String,
                        selectedData: ItemDropdown
                    ) {
                        selectedStatus = item
                    }
                })
            }
        })

        addAssetViewModel.collectionLocationResponse.observe(this@AddAssetActivity, {response->
            Log.d(TAG, "collection Location response : $response")

            val collectionItemDropdown = retrieveListItemDropdownLocation(response.results)

            binding.idvLocationAsset.apply {
                setHint(getString(R.string.dropdownHintLocation))
                setData(collectionItemDropdown)
                setListener(object: InputDropdownView.DropdownListener{
                    override fun onItemSelected(
                        position: Int,
                        item: String,
                        selectedData: ItemDropdown
                    ) {
                        selectedLocation = item
                    }
                })
            }
        })

        binding.btnSubmitAsset.setOnClickListener {
            Log.d(TAG, "status: $selectedStatus, location: $selectedLocation")
        }

    }

    private fun setLayoutForLoading(isLoading: Boolean){
        if(isLoading) {
            binding.loadingLayout.visibility = View.VISIBLE
            binding.pbLoading.visibility = View.VISIBLE
        } else {
            binding.loadingLayout.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE
        }
    }

    private fun setLayoutForPopUp(isShown: Boolean){
        if(isShown){
            binding.loadingLayout.visibility = View.VISIBLE
            binding.pbLoading.visibility = View.GONE
        }else{
            binding.loadingLayout.visibility = View.GONE
            binding.pbLoading.visibility = View.GONE
        }
    }
}
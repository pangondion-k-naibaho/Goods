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
import com.goods.client.data.repository.create_asset.CreateAssetRepositoryImpl
import com.goods.client.databinding.ActivityAddAssetBinding
import com.goods.client.ui.activities.dashboard.DashboardActivity
import com.goods.client.ui.activities.login.LoginActivity
import com.goods.client.ui.custom_components.CustomActionbar
import com.goods.client.ui.custom_components.InputDropdownView
import com.goods.client.ui.custom_components.InputTextView
import com.goods.client.ui.custom_components.PopUpNotificationListener
import com.goods.client.ui.custom_components.showPopUpNitification
import com.goods.client.ui.viewmodels.addedit_asset.AddEditAssetViewModel
import com.goods.client.ui.viewmodels.addedit_asset.AddEditAssetViewModelFactory
import com.goods.client.utils.Extensions.Companion.retrieveListItemDropdownLocation
import com.goods.client.utils.Extensions.Companion.retrieveListItemDropdownStatus

class AddAssetActivity : AppCompatActivity() {
    private val TAG = AddAssetActivity::class.java.simpleName
    private lateinit var binding: ActivityAddAssetBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var addEditAssetViewModel: AddEditAssetViewModel
    private var userToken = ""

    private var retrievedAssetName: String?= null
    private var retrievedStatusId: String?= null
    private var retrievedLocationId: String?= null
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
        val createAssetRepository = CreateAssetRepositoryImpl(apiService)

        val factory = AddEditAssetViewModelFactory(collectionStatusRepository, collectionLocationRepository, createAssetRepository)
        addEditAssetViewModel = ViewModelProvider(this@AddAssetActivity, factory)[AddEditAssetViewModel::class.java]

        observeStatus()
        setUpActionbar()
        initView()
    }

    private fun observeStatus(){
        addEditAssetViewModel.isLoading.observe(this@AddAssetActivity, {
            setLayoutForLoading(it)
        })

        addEditAssetViewModel.isFail.observe(this@AddAssetActivity, {
            Toast.makeText(this@AddAssetActivity, "Unable to retrieve data...", Toast.LENGTH_SHORT).show()
        })

        addEditAssetViewModel.isUnauthorized.observe(this@AddAssetActivity, {
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

        addEditAssetViewModel.isCreateAssetFail.observe(this@AddAssetActivity, {
            if(it){
                showPopUpNitification(
                    textTitle = getString(R.string.popupCreateAssetFailedTitle),
                    textDesc = getString(R.string.popupCreateAssetFailedDesc),
                    backgroundImage = R.drawable.ic_fail,
                    listener = object: PopUpNotificationListener{
                        override fun onPopUpClosed() {}
                    }
                )
            }
        })

        addEditAssetViewModel.createAssetResponse2.observe(this@AddAssetActivity, { response->
            Log.d(TAG, "createAssetResponse: $response")
            if(response != null){
                showPopUpNitification(
                    textTitle = getString(R.string.popupCreateAssetSuccessTitle),
                    textDesc = getString(R.string.popipCreateAssetSuccessDesc),
                    backgroundImage = R.drawable.ic_success,
                    listener = object: PopUpNotificationListener{
                        override fun onPopUpClosed() {
                            startActivity(AddAssetActivity.newIntent(this@AddAssetActivity))
                            finish()
                        }
                    }
                )
            }
        })
    }

    private fun setUpActionbar(){
        binding.cabAddAsset.apply {
            setTitle(getString(R.string.cabInputAssetTitle))
            setListener(object: CustomActionbar.ActionbarListener{
                override fun onButtonLeftClicked() {
                    startActivity(DashboardActivity.newIntent(this@AddAssetActivity))
                    finish()
                }
            })
        }
    }

    private fun initView(){
        sharedPreferences = this@AddAssetActivity.getSharedPreferences(Constants.PREFERENCES.APP_PREFERENCES, Context.MODE_PRIVATE)

        userToken = sharedPreferences.getString(Constants.PREFERENCES.TOKEN_KEY, "")!!

        addEditAssetViewModel.getStatusCollection(userToken)
        addEditAssetViewModel.getLocationCollection(userToken)

        addEditAssetViewModel.collectionStatusResponse.observe(this@AddAssetActivity, { response->
            Log.d(TAG, "collection Status Response: $response")
            val collectionItemDropdown = retrieveListItemDropdownStatus(response.results)

            binding.idvStatusAsset.apply {
                setTitle(getString(R.string.dropdownTitleStatus))
                setHint(getString(R.string.dropdownHintStatus))
                setData(collectionItemDropdown)
                setListener(object: InputDropdownView.DropdownListener{

                    override fun onDropdownClicked() {
                        setNormal()
                        binding.itvAssetName.clearingFocus()
                    }

                    override fun onDismissPopUp() {
                        if(selectedStatus == null){
                            setError()
                        }
                    }

                    override fun onItemSelected(
                        position: Int,
                        item: String,
                        selectedData: ItemDropdown
                    ) {
                        selectedStatus = item
                        retrievedStatusId = selectedData.id
                        setText(item)
                        setNormal()
                    }
                })
            }
        })

        addEditAssetViewModel.collectionLocationResponse.observe(this@AddAssetActivity, { response->
            Log.d(TAG, "collection Location response : $response")

            val collectionItemDropdown = retrieveListItemDropdownLocation(response.results)

            binding.idvLocationAsset.apply {
                setTitle(getString(R.string.dropdownTitleLocation))
                setHint(getString(R.string.dropdownHintLocation))
                setData(collectionItemDropdown)
                setListener(object: InputDropdownView.DropdownListener{

                    override fun onDropdownClicked() {
                        setNormal()
                        binding.itvAssetName.clearingFocus()
                    }

                    override fun onDismissPopUp() {
                        if(selectedLocation == null){
                            setError()
                        }
                    }

                    override fun onItemSelected(
                        position: Int,
                        item: String,
                        selectedData: ItemDropdown
                    ) {
                        selectedLocation = item
                        retrievedLocationId = selectedData.id
                        setText(item)
                        setNormal()
                    }
                })
            }
        })

        binding.itvAssetName.apply {
            setTitle(getString(R.string.tvInputAssetName))
            setTextHelper(getString(R.string.tvInputHintAssetName))
            setInputType(InputTextView.INPUT_TYPE.REGULAR)
            setListener(object: InputTextView.InputViewListener{
                override fun onClickReveal() {}
            })
        }

        binding.btnSubmitAsset.setOnClickListener {
            retrievedAssetName = binding.itvAssetName.getText()

            val assetNameisNull = retrievedAssetName.isNullOrEmpty()
            val assetStatusisNull = selectedStatus.isNullOrEmpty()
            val assetLocationisNull = selectedLocation.isNullOrEmpty()

            when{
                (assetNameisNull && assetStatusisNull && assetLocationisNull) ->{
                    binding.itvAssetName.setOnBlankWarning()
                    binding.idvStatusAsset.setError()
                    binding.idvLocationAsset.setError()
                }
                (!assetNameisNull && assetStatusisNull && assetLocationisNull) ->{
                    binding.idvStatusAsset.setError()
                    binding.idvLocationAsset.setError()
                }
                (assetNameisNull && !assetStatusisNull && assetLocationisNull) ->{
                    binding.itvAssetName.setOnBlankWarning()
                    binding.idvLocationAsset.setError()
                }
                (assetNameisNull && assetStatusisNull && !assetLocationisNull) ->{
                    binding.itvAssetName.setOnBlankWarning()
                    binding.idvStatusAsset.setError()
                }
                (!assetNameisNull && !assetStatusisNull && assetLocationisNull) ->{
                    binding.idvLocationAsset.setError()
                }
                (assetNameisNull && !assetStatusisNull && !assetLocationisNull) ->{
                    binding.itvAssetName.setOnBlankWarning()
                }
                (!assetNameisNull && assetStatusisNull && !assetLocationisNull) ->{
                    binding.idvStatusAsset.setError()
                }
                else ->{
                    //If all forms has alredy filled out, then hitting endpoint happens here
                    addEditAssetViewModel.createAsset(userToken, retrievedAssetName!!, retrievedStatusId!!, retrievedLocationId!!)
                }
            }

            Log.d(TAG, "statusId: $retrievedStatusId, locationId: $retrievedLocationId")
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

    override fun onRestart() {
        super.onRestart()
        retrievedAssetName = null
        retrievedStatusId = null
        retrievedLocationId = null
        selectedStatus = null
        selectedLocation = null
    }
}
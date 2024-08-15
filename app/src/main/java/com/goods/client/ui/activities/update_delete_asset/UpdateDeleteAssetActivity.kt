package com.goods.client.ui.activities.update_delete_asset

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
import com.goods.client.data.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.goods.client.data.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.goods.client.data.model.other.ItemDropdown
import com.goods.client.data.remote.ApiConfig
import com.goods.client.data.repository.collection_location.CollectionLocationRepositoryImpl
import com.goods.client.data.repository.collection_status.CollectionStatusRepositoryImpl
import com.goods.client.data.repository.create_asset.CreateAssetRepositoryImpl
import com.goods.client.data.repository.delete_asset.DeleteAssetRepositoryImpl
import com.goods.client.data.repository.detail_asset.DetailAssetRepositoryImpl
import com.goods.client.data.repository.update_asset.UpdateAssetRepositoryImpl
import com.goods.client.databinding.ActivityAddAssetBinding
import com.goods.client.ui.activities.dashboard.DashboardActivity
import com.goods.client.ui.activities.login.LoginActivity
import com.goods.client.ui.custom_components.CustomActionbar
import com.goods.client.ui.custom_components.InputDropdownView
import com.goods.client.ui.custom_components.InputTextView
import com.goods.client.ui.custom_components.PopUpNotificationListener
import com.goods.client.ui.custom_components.PopUpQuestionListener
import com.goods.client.ui.custom_components.showPopUpNitification
import com.goods.client.ui.custom_components.showPopUpQuestion
import com.goods.client.ui.viewmodels.crud_asset.CrudAssetViewModel
import com.goods.client.ui.viewmodels.crud_asset.CrudAssetViewModelFactory
import com.goods.client.utils.Extensions

class UpdateDeleteAssetActivity : AppCompatActivity() {
    private val TAG = UpdateDeleteAssetActivity::class.java.simpleName
    private lateinit var binding: ActivityAddAssetBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var crudAssetViewModel: CrudAssetViewModel
    private var userToken = ""
    private var deliveredId = ""

    private var retrievedAssetName: String?= null
    private var retrievedStatusId: String?= null
    private var retrievedLocationId: String?= null
    private var selectedStatus: String?= null
    private var selectedLocation: String?= null

    companion object{
        const val EXTRA_ID = "EXTRA_ID"
        fun newIntent(context: Context, id: String): Intent = Intent(context, UpdateDeleteAssetActivity::class.java)
            .putExtra(EXTRA_ID, id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAssetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        deliveredId = intent.extras!!.getString(EXTRA_ID) as String

        val apiService = ApiConfig.createApiService()
        val collectionStatusRepository = CollectionStatusRepositoryImpl(apiService)
        val collectionLocationRepository = CollectionLocationRepositoryImpl(apiService)
        val createAssetRepository = CreateAssetRepositoryImpl(apiService)
        val detailAssetRepository = DetailAssetRepositoryImpl(apiService)
        val updateAssetRepository = UpdateAssetRepositoryImpl(apiService)
        val deleteAssetRepository = DeleteAssetRepositoryImpl(apiService)

        val factory = CrudAssetViewModelFactory(collectionStatusRepository,
            collectionLocationRepository, createAssetRepository, detailAssetRepository,
            updateAssetRepository, deleteAssetRepository)
        crudAssetViewModel = ViewModelProvider(this@UpdateDeleteAssetActivity, factory)[CrudAssetViewModel::class.java]

        observeStatus()
        setUpActionbarnBottomButton()
        initView()
    }

    private fun observeStatus(){
        crudAssetViewModel.isLoading.observe(this@UpdateDeleteAssetActivity, {
            setLayoutForLoading(it)
        })

        crudAssetViewModel.isFail.observe(this@UpdateDeleteAssetActivity, {
            Toast.makeText(this@UpdateDeleteAssetActivity, "Unable to retrieve data...", Toast.LENGTH_SHORT).show()
        })

        crudAssetViewModel.isUnauthorized.observe(this@UpdateDeleteAssetActivity, {
            if(it){
                setLayoutForPopUp(true)
                showPopUpNitification(
                    textTitle = getString(R.string.popupUnauthorizedTitle),
                    textDesc = getString(R.string.popupUnauthorizedDesc),
                    backgroundImage = R.drawable.ic_fail,
                    listener = object: PopUpNotificationListener {
                        override fun onPopUpClosed() {
                            setLayoutForPopUp(false)
                            startActivity(LoginActivity.newIntent(this@UpdateDeleteAssetActivity))
                            this@UpdateDeleteAssetActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            this@UpdateDeleteAssetActivity.finish()
                        }
                    }
                )
            }
        })

        crudAssetViewModel.isUpdateAssetFail.observe(this@UpdateDeleteAssetActivity, {
            if(it){
                setLayoutForPopUp(true)
                showPopUpNitification(
                    textTitle = getString(R.string.popupCreateAssetFailedTitle),
                    textDesc = getString(R.string.popupUpdateAssetFailedDesc),
                    backgroundImage = R.drawable.ic_fail,
                    listener = object: PopUpNotificationListener{
                        override fun onPopUpClosed() {
                            setLayoutForPopUp(false)
                        }
                    }
                )
            }
        })

        crudAssetViewModel.createUpdateAssetResponse3.observe(this@UpdateDeleteAssetActivity, { response->
            Log.d(TAG, "createAssetResponse: $response")
            if(response != null){
                setLayoutForPopUp(true)
                showPopUpNitification(
                    textTitle = getString(R.string.popupCreateAssetSuccessTitle),
                    textDesc = getString(R.string.popupUpdateAssetSuccessDesc),
                    backgroundImage = R.drawable.ic_success,
                    listener = object: PopUpNotificationListener{
                        override fun onPopUpClosed() {
                            setLayoutForPopUp(false)
                            startActivity(DashboardActivity.newIntent(this@UpdateDeleteAssetActivity))
                            finish()
                        }
                    }
                )
            }
        })

        crudAssetViewModel.deleteAssetResponse.observe(this@UpdateDeleteAssetActivity, {
            setLayoutForPopUp(true)
            showPopUpNitification(
                textTitle = getString(R.string.popupDeleteAssetSuccessTitle),
                textDesc = getString(R.string.popupDeleteAssetSuccessDesc),
                backgroundImage = R.drawable.ic_success,
                listener = object: PopUpNotificationListener{
                    override fun onPopUpClosed() {
                        setLayoutForPopUp(false)
                        startActivity(
                            DashboardActivity.newIntent(this@UpdateDeleteAssetActivity)
                        )
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()
                    }
                }
            )
        })

        crudAssetViewModel.isDeleteAssetFail.observe(this@UpdateDeleteAssetActivity, {
            setLayoutForPopUp(true)
            showPopUpNitification(
                textTitle = getString(R.string.popupDeleteAssetSuccessTitle),
                textDesc = getString(R.string.popupDeleteAssetSuccessDesc),
                backgroundImage = R.drawable.ic_success,
                listener = object: PopUpNotificationListener{
                    override fun onPopUpClosed() {
                        setLayoutForPopUp(false)
                    }
                }
            )
        })

    }

    private fun setUpActionbarnBottomButton(){
        binding.cabAddAsset.apply {
            setTitle(getString(R.string.cabInputAssetTitle))
            setListener(object: CustomActionbar.ActionbarListener{
                override fun onButtonLeftClicked() {
                    startActivity(DashboardActivity.newIntent(this@UpdateDeleteAssetActivity))
                    finish()
                }
            })
        }

        binding.btnSubmitAsset.visibility = View.GONE
        binding.btnSaveEdit.visibility = View.VISIBLE
        binding.btnDeleteEdit.visibility = View.VISIBLE
    }

    private fun initView(){
        sharedPreferences = this@UpdateDeleteAssetActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        userToken = sharedPreferences.getString(TOKEN_KEY, "")!!

        crudAssetViewModel.getDetailAsset(userToken, deliveredId)
        crudAssetViewModel.getStatusCollection(userToken)
        crudAssetViewModel.getLocationCollection(userToken)

        crudAssetViewModel.detailAssetResponse.observe(this@UpdateDeleteAssetActivity, { response->
            val detailAssetResponse = response

            retrievedAssetName = response.name
            retrievedLocationId = response.location.id
            retrievedStatusId = response.status.id
            selectedLocation = response.location.name
            selectedStatus = response.status.name

            crudAssetViewModel.collectionStatusResponse.observe(this@UpdateDeleteAssetActivity, { response->

                val collectionItemDropdown = Extensions.retrieveListItemDropdownStatus(response.results)

                binding.idvStatusAsset.apply {
                    setTitle(getString(R.string.dropdownTitleStatus))
                    setHint(detailAssetResponse.status.name)
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

            crudAssetViewModel.collectionLocationResponse.observe(this@UpdateDeleteAssetActivity,{ response->
                val collectionItemDropdown =
                    Extensions.retrieveListItemDropdownLocation(response.results)

                binding.idvLocationAsset.apply {
                    setTitle(getString(R.string.dropdownTitleLocation))
                    setHint(detailAssetResponse.location.name)
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
                setText(detailAssetResponse.name)
                setTextHelper(getString(R.string.tvInputHintAssetName))
                setInputType(InputTextView.INPUT_TYPE.REGULAR)
                setListener(object: InputTextView.InputViewListener{
                    override fun onClickReveal() {}
                })
            }
        })

        binding.btnSaveEdit.setOnClickListener {
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
                    crudAssetViewModel.updateAsset(userToken, deliveredId, retrievedAssetName!!, retrievedStatusId!!, retrievedLocationId!!)
                }
            }

            Log.d(TAG, "id_asset: $deliveredId, name: $retrievedAssetName, statusId: $retrievedStatusId, locationId: $retrievedLocationId")
        }

        binding.btnDeleteEdit.setOnClickListener {
            setLayoutForPopUp(true)
            showPopUpQuestion(
                textTitle = getString(R.string.popupQuestionDeleteAssetTitle),
                textDesc = getString(R.string.popupQuestionDeleteAssetDesc),
                textBtnPositive = getString(R.string.btnPopupQuestionDeleteAssetPositive),
                textBtnNegative = getString(R.string.btnPopupQuestionDeleteAssetNegative),
                listener = object: PopUpQuestionListener{
                    override fun onPostiveClicked() {
                        closeOptionsMenu()
                        setLayoutForPopUp(false)
                        crudAssetViewModel.deleteAsset(userToken, deliveredId)
                    }

                    override fun onNegativeClicked() {
                        closeOptionsMenu()
                        setLayoutForPopUp(false)
                    }

                }
            )
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
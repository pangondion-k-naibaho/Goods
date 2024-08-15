package com.goods.client.ui.viewmodels.crud_asset

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goods.client.data.model.request.create_update_asset.CreateUpdateAssetRequest
import com.goods.client.data.model.response.collection_location.CollectionLocationResponse
import com.goods.client.data.model.response.collection_status.CollectionStatusResponse
import com.goods.client.data.model.response.create_update_asset.CreateUpdateAssetResponse
import com.goods.client.data.model.response.detail_asset.DetailAssetResponse
import com.goods.client.data.repository.collection_location.CollectionLocationRepository
import com.goods.client.data.repository.collection_status.CollectionStatusRepository
import com.goods.client.data.repository.create_asset.CreateAssetRepository
import com.goods.client.data.repository.delete_asset.DeleteAssetRepository
import com.goods.client.data.repository.detail_asset.DetailAssetRepository
import com.goods.client.data.repository.update_asset.UpdateAssetRepository
import kotlinx.coroutines.launch

class CrudAssetViewModel(
    private val collectionStatusRepository: CollectionStatusRepository,
    private val collectionLocationRepository: CollectionLocationRepository,
    private val createAssetRepository: CreateAssetRepository,
    private val detailAssetRepository: DetailAssetRepository,
    private val updateAssetRepository: UpdateAssetRepository,
    private val deleteAssetRepository: DeleteAssetRepository
): ViewModel(){
    private val TAG = CrudAssetViewModel::class.java.simpleName

    private var _collectionStatusResponse = MutableLiveData<CollectionStatusResponse>()
    val collectionStatusResponse: LiveData<CollectionStatusResponse> = _collectionStatusResponse

    private var _collectionLocationResponse = MutableLiveData<CollectionLocationResponse>()
    val collectionLocationResponse: LiveData<CollectionLocationResponse> = _collectionLocationResponse

    private var _createAssetResponse = MutableLiveData<String>()
    val createAssetResponse: LiveData<String> = _createAssetResponse

    private var _createUpdateAssetResponse2 = MutableLiveData<CreateUpdateAssetResponse>()
    val createUpdateAssetResponse2: LiveData<CreateUpdateAssetResponse> = _createUpdateAssetResponse2

    private var _createUpdateAssetResponse3 = MutableLiveData<CreateUpdateAssetResponse>()
    val createUpdateAssetResponse3: LiveData<CreateUpdateAssetResponse> = _createUpdateAssetResponse3

    private var _detailAssetResponse = MutableLiveData<DetailAssetResponse>()
    val detailAssetResponse: LiveData<DetailAssetResponse> = _detailAssetResponse

    private var _deleteAssetResponse = MutableLiveData<Unit>()
    val deleteAssetResponse : LiveData<Unit> = _deleteAssetResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail : LiveData<Boolean> = _isFail

    private var _isCreateAssetFail = MutableLiveData<Boolean>()
    val isCreateAssetFail : LiveData<Boolean> = _isCreateAssetFail

    private var _isUpdateAssetFail = MutableLiveData<Boolean>()
    val isUpdateAssetFail : LiveData<Boolean> = _isUpdateAssetFail

    private var _isDeleteAssetFail = MutableLiveData<Boolean>()
    val isDeleteAssetFail: LiveData<Boolean> = _isDeleteAssetFail

    private var _isUnauthorized = MutableLiveData<Boolean>()
    val isUnauthorized: LiveData<Boolean> = _isUnauthorized

    fun getStatusCollection(token: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = collectionStatusRepository.getCollectionStatus("Bearer ${token}")
            if(result.isSuccess){
                _collectionStatusResponse.value = result.getOrNull()
                Log.d(TAG, "getStatusCollection Success")
            }else{
                val message = result.exceptionOrNull()!!.message
                if(message!!.contains("401")) _isUnauthorized.value = true else _isFail.value = true
            }
            _isLoading.value = false
        }
    }

    fun getLocationCollection(token: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = collectionLocationRepository.getCollectionLocation("Bearer $token")
            if(result.isSuccess){
                _collectionLocationResponse.value = result.getOrNull()
                Log.d(TAG, "getLocationCollection Success")
            }else{
                val message = result.exceptionOrNull()!!. message
                if(message!!.contains("401")) _isUnauthorized.value = true else _isFail.value = true
            }
            _isLoading.value = false
        }
    }

    fun createAsset(tokenAuth: String, assetName: String, assetStatusId: String, assetLocationId: String){
        _isLoading.value = true
        viewModelScope.launch {
            val assetRequest = CreateUpdateAssetRequest(assetName, assetStatusId, assetLocationId)
            val result = createAssetRepository.createAsset("Bearer $tokenAuth", assetRequest)
            if(result.isSuccess){
                _createUpdateAssetResponse2.value = result.getOrNull()
                Log.d(TAG, "createAsset Success")
            }else{
                val message = result.exceptionOrNull()!!.message
                if(message!!.contains("401")) _isUnauthorized.value = true else _isCreateAssetFail.value = true
            }
            _isLoading.value = false
        }
    }

    fun getDetailAsset(tokenAuth: String, id: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = detailAssetRepository.getDetailAsset("Bearer $tokenAuth", id)
            if(result.isSuccess){
                _detailAssetResponse.value = result.getOrNull()
                Log.d(TAG, "getDetailAsset Success")
            }else{
                val message = result.exceptionOrNull()!!.message
                if(message!!.contains("401")) _isUnauthorized.value = true else _isFail.value = true
            }
            _isLoading.value = false
        }
    }

    fun updateAsset(tokenAuth: String, assetId: String, assetName: String, assetStatusId: String, assetLocationId: String){
        _isLoading.value = true
        viewModelScope.launch {
            val assetRequest = CreateUpdateAssetRequest(assetName, assetStatusId, assetLocationId)
            val result = updateAssetRepository.updateAsset("Bearer $tokenAuth", assetId, assetRequest)
            if(result.isSuccess){
                _createUpdateAssetResponse3.value = result.getOrNull()
                Log.d(TAG, "editAsset Success")
            }else{
                val message = result.exceptionOrNull()!!.message
                Log.e(TAG, "$message")
                if(message!!.contains("401")) _isUnauthorized.value = true else _isUpdateAssetFail.value = true
            }
            _isLoading.value = false
        }
    }

    fun deleteAsset(tokenAuth: String, assetId: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = deleteAssetRepository.deleteAsset("Bearer $tokenAuth", assetId)
            if(result.isSuccess){
                _deleteAssetResponse.value = result.getOrNull()
                Log.d(TAG, "deleteAsset Success")
            }else{
                val message = result.exceptionOrNull()!!.message
                Log.e(TAG, "$message")
                if(message!!.contains("401")) _isUnauthorized.value = true else _isDeleteAssetFail.value = true
            }
            _isLoading.value = false
        }
    }
}
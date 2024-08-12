package com.goods.client.ui.viewmodels.asset

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goods.client.data.model.response.all_asset.AllAssetResponse
import com.goods.client.data.repository.all_asset.AllAssetRepository
import kotlinx.coroutines.launch

class AssetViewModel(
    private val allAssetRepository: AllAssetRepository,
): ViewModel() {
    private val TAG = AssetViewModel::class.java.simpleName

    private var _allAssetResponse = MutableLiveData<AllAssetResponse>()
    val allAssetResponse: LiveData<AllAssetResponse> = _allAssetResponse

    private var _allAssetResponse2 = MutableLiveData<AllAssetResponse>()
    val allAssetResponse2: LiveData<AllAssetResponse> = _allAssetResponse2

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    fun getAllAsset(token: String, page: Int, search: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = allAssetRepository.getAllAsset(tokenAuth = "Bearer $token", page = page, pageSize = null, search = search)
            if(result.isSuccess){
                _allAssetResponse.value = result.getOrNull()
                Log.d(TAG, "Success")
            }else{
                _isFail.value = true
                Log.e(TAG, "error message: ${result.exceptionOrNull()?.message}")
            }
            _isLoading.value = false
        }
    }

    fun getAllAssetMore(token: String, page: Int, search: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = allAssetRepository.getAllAsset(tokenAuth = "Bearer $token", page = page, pageSize = null, search = search)
            if(result.isSuccess){
                _allAssetResponse2.value = result.getOrNull()
                Log.d(TAG, "Success")
            }else{
                _isFail.value = true
                Log.e(TAG, "error message: ${result.exceptionOrNull()?.message}")
            }
            _isLoading.value = false
        }
    }

}
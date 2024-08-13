package com.goods.client.ui.viewmodels.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goods.client.data.model.response.asset_by_status.CollectionAssetStatusResponse
import com.goods.client.data.repository.asset_by_status.AssetStatusRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val assetStatusRepository: AssetStatusRepository,
): ViewModel() {
    private val TAG = HomeViewModel::class.java.simpleName

    private var _collectionAssetStatusResponse = MutableLiveData<CollectionAssetStatusResponse>()
    val collectionAssetStatusResponse: LiveData<CollectionAssetStatusResponse> = _collectionAssetStatusResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    private var _isUnauthorized = MutableLiveData<Boolean>()
    val isUnauthorized: LiveData<Boolean> = _isUnauthorized

    fun getAssetByStatus(token: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = assetStatusRepository.getAssetByStatus("Bearer ${token}")
            if(result.isSuccess){
                _collectionAssetStatusResponse.value = result.getOrNull()
                Log.d(TAG, "Success")
            }else{
                val message = result.exceptionOrNull()!!.message
                if(message!!.contains("401")) _isUnauthorized.value = true else _isFail.value = true
                Log.e(TAG, "$message")
            }
            _isLoading.value = false
        }
    }
}
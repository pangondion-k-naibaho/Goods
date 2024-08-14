package com.goods.client.ui.viewmodels.add_asset

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goods.client.data.model.response.collection_location.CollectionLocationResponse
import com.goods.client.data.model.response.collection_status.CollectionStatusResponse
import com.goods.client.data.repository.collection_location.CollectionLocationRepository
import com.goods.client.data.repository.collection_status.CollectionStatusRepository
import kotlinx.coroutines.launch

class AddAssetViewModel(
    private val collectionStatusRepository: CollectionStatusRepository,
    private val collectionLocationRepository: CollectionLocationRepository
): ViewModel(){
    private val TAG = AddAssetViewModel::class.java.simpleName

    private var _collectionStatusResponse = MutableLiveData<CollectionStatusResponse>()
    val collectionStatusResponse: LiveData<CollectionStatusResponse> = _collectionStatusResponse

    private var _collectionLocationResponse = MutableLiveData<CollectionLocationResponse>()
    val collectionLocationResponse: LiveData<CollectionLocationResponse> = _collectionLocationResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail : LiveData<Boolean> = _isFail

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
}
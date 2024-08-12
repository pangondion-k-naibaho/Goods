package com.goods.client.ui.viewmodels.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goods.client.data.model.response.logout.LogoutResponse
import com.goods.client.data.model.response.profile.ProfileResponse
import com.goods.client.data.repository.logout.LogoutRepository
import com.goods.client.data.repository.profile.ProfileRepository
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val profileRepository: ProfileRepository,
    private val logoutRepository: LogoutRepository,
): ViewModel() {
    private val TAG = DashboardViewModel::class.java.simpleName

    private var _profileResponse = MutableLiveData<ProfileResponse>()
    val profileResponse: LiveData<ProfileResponse> = _profileResponse

    private var _logoutResponse = MutableLiveData<LogoutResponse>()
    val logoutResponse: LiveData<LogoutResponse> = _logoutResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    private var _isLogoutFail = MutableLiveData<Boolean>()
    val isLogoutFail: LiveData<Boolean> = _isLogoutFail

    fun getProfile(token: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = profileRepository.getProfile("Bearer $token")
            if(result.isSuccess){
                _profileResponse.value = result.getOrNull()
                Log.d(TAG, "Success")
            }else{
                _isFail.value = true
                Log.e(TAG, "error message: ${result.exceptionOrNull()?.message}")
            }
            _isLoading.value = false
        }
    }

    fun logoutUser(token: String){
        _isLoading.value = true
        viewModelScope.launch {
            val result = logoutRepository.logoutUser("Bearer $token")
            if(result.isSuccess){
                _logoutResponse.value = result.getOrNull()
                Log.d(TAG, "Success")
            }else{
                _isLogoutFail.value = true
                Log.e(TAG, "error message: ${result.exceptionOrNull()?.message}")
            }
            _isLoading.value = false
        }
    }
}
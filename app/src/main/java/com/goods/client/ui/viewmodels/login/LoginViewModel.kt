package com.goods.client.ui.viewmodels.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goods.client.data.model.response.login.LoginResponse
import com.goods.client.data.repository.login.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val TAG = LoginViewModel::class.java.simpleName

    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

//    fun loginUser(email: String, password: String) {
//        _isLoading.value = true
//        viewModelScope.launch {
//            val result = loginRepository.login(email, password)
//            _loginResult.value = result
//            _isLoading.value = false
//        }
//    }

    fun loginUser(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = loginRepository.login(email, password)
            if (result.isSuccess) {
                _loginResponse.value = result.getOrNull()
                Log.d(TAG, "Success")
//                _errorMessage.value = "no error message"
            } else {
                _isFail.value = true
                Log.e(TAG, "error message :${result.exceptionOrNull()?.message}")
//                _errorMessage.value = result.exceptionOrNull()?.message
            }
            _isLoading.value = false
        }
    }

}

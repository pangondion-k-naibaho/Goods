package com.goods.client.ui.activities.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.goods.client.R
import com.goods.client.data.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.goods.client.data.Constants.PREFERENCES.Companion.EMAIL_KEY
import com.goods.client.data.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.goods.client.data.Constants.PREFERENCES.Companion.USERNAME_KEY
import com.goods.client.data.remote.ApiConfig
import com.goods.client.data.repository.login.LoginRepositoryImpl
import com.goods.client.databinding.ActivityLoginBinding
import com.goods.client.ui.activities.dashboard.DashboardActivity
import com.goods.client.ui.custom_components.InputTextView
import com.goods.client.ui.custom_components.PopUpNotificationListener
import com.goods.client.ui.custom_components.showPopUpNitification
import com.goods.client.ui.viewmodels.login.LoginViewModel
import com.goods.client.ui.viewmodels.login.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {
    private val TAG = LoginActivity::class.java.simpleName
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    companion object{
        fun newIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ApiService dan LoginRepository
        val apiService = ApiConfig.createApiService()
        val loginRepository = LoginRepositoryImpl(apiService)

        // Menggunakan LoginViewModelFactory untuk inisialisasi LoginViewModel
        val factory = LoginViewModelFactory(loginRepository)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        observeViewModel()

        initView()
    }

    private fun observeViewModel(){
        loginViewModel.isLoading.observe(this@LoginActivity, {
            setLayoutForLoading(it)
        })

        loginViewModel.loginResult.observe(this@LoginActivity, { result->
            if(result.isSuccess){
                val loginResponse = result.getOrNull()
                val appPreferences = this@LoginActivity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
                val editor = appPreferences.edit()
                editor.putString(TOKEN_KEY, loginResponse!!.token)
                editor.putString(EMAIL_KEY, loginResponse.email)
                editor.putString(USERNAME_KEY, loginResponse.username)
                editor.apply()
                if(editor.commit()){
                    setLayoutForPopUp(true)
                    this@LoginActivity.showPopUpNitification(
                        textTitle = getString(R.string.popupLoginSuccessTitle),
                        textDesc = getString(R.string.popupLoginSuccessDesc),
                        backgroundImage = R.drawable.ic_success,
                        listener = object: PopUpNotificationListener{
                            override fun onPopUpClosed() {
                                startActivity(DashboardActivity.newIntent(this@LoginActivity))
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                                finish()
                            }
                        }
                    )
                }
            }else{
//                val codeResult = result.getOrNull().
                setLayoutForPopUp(true)
                this@LoginActivity.showPopUpNitification(
                    textTitle = getString(R.string.popupLoginFailedTitle),
                    textDesc = getString(R.string.popupLoginFailedDesc),
                    backgroundImage = R.drawable.ic_fail,
                )
            }
        })
    }

    private fun initView(){
        binding.itvEmail.apply {
            setTextHelper(getString(R.string.tvETHintEmail))
            setInputType(InputTextView.INPUT_TYPE.EMAIL)
        }

        binding.itvPassword.apply {
            setTextHelper(getString(R.string.tvETHintPassword))
            setInputType(InputTextView.INPUT_TYPE.PASSWORD)

            val listener = object: InputTextView.InputViewListener{
                override fun onClickReveal() {
                    revealPassword()
                }
            }

            setListener(listener)
        }

        binding.btnLogin.apply {
            setOnClickListener{
                binding.apply {
                    binding.itvEmail.clearingFocus()
                    binding.itvPassword.clearingFocus()
                }
                val email = binding.itvEmail.getText()
                val password = binding.itvPassword.getText()

                when{
                    email.isNullOrEmpty() && !password.isNullOrEmpty() -> binding.itvEmail.setOnBlankWarning()
                    !password.isNullOrEmpty() && password.isNullOrEmpty() -> binding.itvPassword.setOnBlankWarning()
                    email.isNullOrEmpty() && password.isNullOrEmpty() ->{
                        binding.itvEmail.setOnBlankWarning()
                        binding.itvPassword.setOnBlankWarning()
                    }
                    else -> {
                        Log.d(TAG,"retrieved Email: ${email}, retrievedPassword: ${password}")
                        loginViewModel.loginUser(email, password)
                    }
                }
            }
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
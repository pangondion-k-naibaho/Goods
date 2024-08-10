package com.goods.client.ui.activities.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.goods.client.R
import com.goods.client.databinding.ActivityLoginBinding
import com.goods.client.ui.custom_components.InputTextView

class LoginActivity : AppCompatActivity() {
    private val TAG = LoginActivity::class.java.simpleName
    private lateinit var binding: ActivityLoginBinding

    companion object{
        fun newIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
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

                    }
                }
            }
        }
    }

    private fun setLayoutForLoading(isLoading: Boolean){
        if(isLoading) binding.loadingLayout.visibility = View.VISIBLE else binding.loadingLayout.visibility = View.GONE
    }
}
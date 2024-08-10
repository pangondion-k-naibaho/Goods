package com.goods.client.ui.custom_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.goods.client.R
import com.goods.client.databinding.HomeActionbarLayoutBinding

class HomeActionBar: ConstraintLayout{
    private lateinit var binding: HomeActionbarLayoutBinding
    private var listener: HomeActionbarListener?= null

    private val TAG = HomeActionBar::class.java.simpleName

    constructor(context: Context):super(context){
        init(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet):super(context, attributeSet){
        init(context, attributeSet)
    }

    private fun init(context: Context, attributeSet: AttributeSet?){
        binding = HomeActionbarLayoutBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.home_actionbar_layout, this, true)
        )

        binding.btnLogout.setOnClickListener {
            listener?.onLogoutClicked()
        }
    }

    fun setUsername(input: String){
        binding.tvUsername.text = input
    }

    fun setEmail(input: String){
        binding.tvEmail.text = input
    }

    fun setListener(input: HomeActionbarListener){
        listener = input
    }

    interface HomeActionbarListener{
        fun onLogoutClicked()
    }
}
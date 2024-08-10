package com.goods.client.ui.custom_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.goods.client.R
import com.goods.client.databinding.CustomActionbarLayoutBinding

class CustomActionbar: ConstraintLayout {
    private lateinit var binding: CustomActionbarLayoutBinding
    private var actionbarListener: ActionbarListener?= null

    private val TAG = CustomActionbar::class.java.simpleName

    constructor(context: Context):super(context){
        init(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet):super(context, attributeSet){
        init(context, attributeSet)
    }

    private fun init(context: Context, attributeSet: AttributeSet?){
        binding = CustomActionbarLayoutBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.custom_actionbar_layout, this, true)
        )

        binding.btnLeft.setOnClickListener {
            actionbarListener?.onButtonLeftClicked()
        }
    }

    fun setTitle(input: String){
        binding.tvTitle.text = input
    }

    fun setListener(listener: ActionbarListener){
        actionbarListener = listener
    }

    fun setActionbarVisible(){
        binding.root.visibility = View.VISIBLE
    }

    fun setActionbarInvisible(){
        binding.root.visibility = View.GONE
    }

    interface ActionbarListener{
        fun onButtonLeftClicked(){}
    }
}
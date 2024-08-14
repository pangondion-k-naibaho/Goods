package com.goods.client.ui.custom_components

import android.content.Context
import android.hardware.display.DisplayManager
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.goods.client.R
import com.goods.client.data.model.other.ItemDropdown
import com.goods.client.databinding.DropdownLayoutBinding
import com.goods.client.databinding.DropdownPopupLayoutBinding
import com.goods.client.ui.rv_adapters.ItemDropdownAdapter

class CustomDropdown: ConstraintLayout{
    private lateinit var mContext: Context
    private lateinit var binding: DropdownLayoutBinding
    private var retrievedItem: String = ""

    private var dropdownListener: DropdownListener?= null
    private var listData: List<ItemDropdown>?= null
    private lateinit var popUpBinding: DropdownPopupLayoutBinding
    private lateinit var popUpWindow: PopupWindow
    private var isDropdownShown: Boolean = false
    private lateinit var itemDropdownAdapter: ItemDropdownAdapter
    private var itemListener: ItemDropdownAdapter.ItemListener?= null

    constructor(context: Context): super(context){
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet):super(context, attrs){
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?){
        mContext = context
        binding = DropdownLayoutBinding.bind(
            LayoutInflater.from(mContext)
                .inflate(R.layout.dropdown_layout, this, true)
        )

        binding.apply {
            spContent.setOnClickListener {
                dropdownListener?.onDropdownClicked()
                if(listData!!.size > 0){
                    performClick()
                    if(!popUpWindow.isShowing){
                        showPopUp()
                    }else{
                        dismissPopUp()
                    }
                }
                isDropdownShown = !isDropdownShown
            }

            val popUpView = LayoutInflater.from(mContext).inflate(R.layout.dropdown_popup_layout, null)
            popUpBinding = DropdownPopupLayoutBinding.bind(popUpView)

            val displayMetrics = DisplayMetrics()
            val displayService = mContext.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager

            displayService.getDisplay(Display.DEFAULT_DISPLAY).getRealMetrics(displayMetrics)

            popUpWindow = PopupWindow(
                popUpView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                true
            )

            popUpWindow.setOnDismissListener {
                dropdownListener!!.onDismissPopUp()
            }

            itemDropdownAdapter = ItemDropdownAdapter(listData!!, itemListener)

        }
    }

    fun setData(listItem: List<ItemDropdown>){
        listData = listItem
        itemDropdownAdapter.updateChecked(listData!![0])
        if(listData!!.size > 0) setText(listData!![0].name!!)
        itemDropdownAdapter = ItemDropdownAdapter(listData!!, object: ItemDropdownAdapter.ItemListener{
            override fun onClick(item: ItemDropdown, position: Int) {
                itemDropdownAdapter.updateChecked(item)
                setText(item.name!!)
                dismissPopUp()
                dropdownListener!!.onItemSelected(position, item.name!!, item)
            }
        })

        popUpBinding.rvItem.apply {
            layoutManager = LinearLayoutManager(context.applicationContext)
            adapter = itemDropdownAdapter
            layoutParams.height = when(itemDropdownAdapter.itemCount < 5){
                true -> ViewGroup.LayoutParams.WRAP_CONTENT
                false -> 400
            }
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        popUpWindow.width = w
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun showPopUp(){
        popUpWindow.showAsDropDown(binding.root, 0, 0)
    }

    private fun dismissPopUp(){
        popUpWindow.dismiss()
    }

    fun setText(field: String){
        binding.spContent.prompt = field
    }

    fun setListener(listener: DropdownListener){
        dropdownListener = listener
    }

    fun setAdapter(adapter: ItemDropdownAdapter){
        itemDropdownAdapter = adapter
    }


    interface DropdownListener{
        fun onDropdownClicked(){}

        fun onItemSelected(position: Int, item: String, selectedData: ItemDropdown){}

        fun onDismissPopUp(){}
    }
}
package com.goods.client.ui.rv_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goods.client.R
import com.goods.client.data.model.response.all_asset.ResultAssetResponse
import com.goods.client.databinding.ItemRvAssetLayoutBinding

class ItemAssetAdapter(
    var data: MutableList<ResultAssetResponse>,
    private val listener: ItemListener
): RecyclerView.Adapter<ItemAssetAdapter.ItemHolder>(){
    interface ItemListener{
        fun onUpdateClicked(item: ResultAssetResponse)
    }

    inner class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = ItemRvAssetLayoutBinding.bind(itemView)
        fun bind(item: ResultAssetResponse, listener: ItemListener) = with(itemView){
            binding.apply {
                tvAssetName.text = item.name
                if(item == data!!.get(data!!.size-1)){
                    assetBorder.visibility = View.GONE
                }else{
                    assetBorder.visibility = View.VISIBLE
                }
                btnUpdateAsset.setOnClickListener {
                    listener.onUpdateClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_asset_layout, parent, false)
        return ItemHolder(view)
    }

    override fun getItemCount(): Int = data!!.size
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(data!!.get(position), listener)
    }

    fun addItem(listAsset: List<ResultAssetResponse>){
        val startPosition = data!!.size
        data!!.addAll(listAsset)
        notifyItemRangeInserted(startPosition, listAsset.size)
    }

    fun updateItem(listAsset: List<ResultAssetResponse>){
        try{
            if(listAsset != null){
                data!!.clear()
                data!!.addAll(listAsset)
                notifyDataSetChanged()
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}
package com.goods.client.ui.activities.dashboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goods.client.R
import com.goods.client.databinding.FragmentAssetBinding
import com.goods.client.ui.activities.dashboard.FragmentsDashboardCommunicator

class AssetFragment : Fragment() {
    private val TAG = AssetFragment::class.java.simpleName
    private var _binding : FragmentAssetBinding?= null
    private val binding get() = _binding!!
    private var userToken: String?= null
    private lateinit var fdCommunicator: FragmentsDashboardCommunicator

    companion object{
        private const val DELIVERED_TOKEN = "DELIVERED_TOKEN"
        fun newInstance(token: String): AssetFragment{
            val fragment = AssetFragment()
            fragment.userToken = token
            val bundle = Bundle()
            bundle.putString(DELIVERED_TOKEN, token)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAssetBinding.inflate(layoutInflater, container, false)
        fdCommunicator = activity as FragmentsDashboardCommunicator

        setUpView()

        return inflater.inflate(R.layout.fragment_asset, container, false)
    }

    private fun setUpView(){

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
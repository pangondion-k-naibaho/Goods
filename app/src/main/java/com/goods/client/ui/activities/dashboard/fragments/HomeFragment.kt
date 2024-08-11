package com.goods.client.ui.activities.dashboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goods.client.R
import com.goods.client.databinding.FragmentHomeBinding
import com.goods.client.ui.activities.dashboard.FragmentsDashboardCommunicator

class HomeFragment : Fragment() {
    private val TAG = HomeFragment::class.java.simpleName
    private var _binding: FragmentHomeBinding?= null
    private val binding get() = _binding!!
    private var userToken: String?= null
    private lateinit var fdCommunicator: FragmentsDashboardCommunicator

    companion object{
        private const val DELIVERED_TOKEN = "DELIVERED_TOKEN"
        fun newInstance(token: String): HomeFragment{
            val fragment = HomeFragment()
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
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        fdCommunicator = activity as FragmentsDashboardCommunicator

        setUpView()

        return binding.root
    }

    private fun setUpView(){

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
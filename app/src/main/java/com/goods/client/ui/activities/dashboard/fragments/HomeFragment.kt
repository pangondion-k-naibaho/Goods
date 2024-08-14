package com.goods.client.ui.activities.dashboard.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.goods.client.R
import com.goods.client.data.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.goods.client.data.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.goods.client.data.remote.ApiConfig
import com.goods.client.data.repository.asset_by_location.AssetLocationRepositoryImpl
import com.goods.client.data.repository.asset_by_status.AssetStatusRepositoryImpl
import com.goods.client.databinding.FragmentHomeBinding
import com.goods.client.ui.activities.dashboard.FragmentsDashboardCommunicator
import com.goods.client.ui.activities.login.LoginActivity
import com.goods.client.ui.custom_components.PopUpNotificationListener
import com.goods.client.ui.custom_components.showPopUpNitification
import com.goods.client.ui.viewmodels.home.HomeViewModel
import com.goods.client.ui.viewmodels.home.HomeViewModelFactory
import com.goods.client.utils.Extensions.Companion.getHalfScreenWidthInFloat
import com.goods.client.utils.Extensions.Companion.retrieveValueBasedOnLocation
import com.goods.client.utils.Extensions.Companion.retrieveValuebasedOnStatus

class HomeFragment : Fragment() {
    private val TAG = HomeFragment::class.java.simpleName
    private var _binding: FragmentHomeBinding?= null
    private val binding get() = _binding!!
    private var userToken: String?= null
    private lateinit var fdCommunicator: FragmentsDashboardCommunicator
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sharedPreferences: SharedPreferences

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

        //Asset by status
        private const val SOLD = "Sold"
        private const val STOCK = "Stock"
        private const val EXPIRED = "Asset"

        //Asset By Location
        private const val GUDANG = "Gudang"
        private const val RAK = "Rak Penjualan"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        fdCommunicator = activity as FragmentsDashboardCommunicator

        val apiService = ApiConfig.createApiService()
        val assetStatusRepository = AssetStatusRepositoryImpl(apiService)
        val assetLocationRepository = AssetLocationRepositoryImpl(apiService)

        val factory = HomeViewModelFactory(assetStatusRepository, assetLocationRepository)
        homeViewModel = ViewModelProvider(this@HomeFragment.requireActivity(), factory)[HomeViewModel::class.java]

        observeStatus()
        setUpView()

        return binding.root
    }

    private fun observeStatus(){
        homeViewModel.isLoading.observe(this@HomeFragment.requireActivity(), {
            if(it) fdCommunicator.startLoading() else fdCommunicator.stopLoading()
        })

        homeViewModel.isFail.observe(this@HomeFragment.requireActivity(), {
            if(it) Toast.makeText(this@HomeFragment.requireActivity(), "Failed to retrieve data...", Toast.LENGTH_SHORT).show()
        })

        homeViewModel.isUnauthorized.observe(this@HomeFragment.requireActivity(), {
            if(it){
                this@HomeFragment.requireActivity().showPopUpNitification(
                    textTitle = getString(R.string.popupUnauthorizedTitle),
                    textDesc = getString(R.string.popupUnauthorizedDesc),
                    backgroundImage = R.drawable.ic_fail,
                    listener =  object: PopUpNotificationListener{
                        override fun onPopUpClosed() {
                            startActivity(LoginActivity.newIntent(this@HomeFragment.requireActivity()))
                            this@HomeFragment.requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            this@HomeFragment.requireActivity().finish()
                        }

                    }
                )
            }
        })
    }

    private fun setUpView(){
        sharedPreferences = this@HomeFragment.requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        userToken = sharedPreferences.getString(TOKEN_KEY, "")

        homeViewModel.getAssetByStatus(userToken!!)
        homeViewModel.getAssetByLocation(userToken!!)

        homeViewModel.collectionAssetStatusResponse.observe(this@HomeFragment.requireActivity(), {response->
            Log.d(TAG, "asset by status response: $response")

            val soldCount = response.retrieveValuebasedOnStatus(SOLD)
            val stockCount = response.retrieveValuebasedOnStatus(STOCK)
            val expiredCount = response.retrieveValuebasedOnStatus(EXPIRED)

            binding.tvStatusValue1.text = soldCount.toString()
            binding.tvStatusValue2.text = stockCount.toString()
            binding.tvStatusValue3.text = expiredCount.toString()

            val barEntries = ArrayList<BarEntry>()
            barEntries.add(BarEntry(0f, soldCount.toFloat())) // Index 0 untuk Sold
            barEntries.add(BarEntry(1f, stockCount.toFloat())) // Index 1 untuk Stock
            barEntries.add(BarEntry(2f, expiredCount.toFloat())) // Index 2 untuk Expired

            val barDataSet = BarDataSet(barEntries, "Kategori")
            barDataSet.colors = listOf(
                this@HomeFragment.requireActivity().getColor(R.color.verditer), // Warna untuk Sold
                this@HomeFragment.requireActivity().getColor(R.color.mythical_orange), // Warna untuk Stock
                this@HomeFragment.requireActivity().getColor(R.color.grenadine_pink)  // Warna untuk Expired (Asset)
            )

            barDataSet.setDrawValues(false)

            val barData = BarData(barDataSet)
            barData.setValueTextColor(this@HomeFragment.requireActivity().getColor(R.color.void_century)) // Atur warna teks
            barData.setValueTextSize(16f) // Ukuran teks
            barData.barWidth = 0.3f

            binding.chartStatus.apply {
                data = barData
                setFitBars(true)

                //Description
                description.isEnabled = false
                axisLeft.textColor = this@HomeFragment.requireActivity().getColor(R.color.void_century)
                axisRight.isEnabled = false
                xAxis.textColor = this@HomeFragment.requireActivity().getColor(R.color.void_century)
                legend.textColor = this@HomeFragment.requireActivity().getColor(R.color.void_century)

                xAxis.granularity = 1f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.setDrawAxisLine(false)
                xAxis.setDrawLabels(false)

                axisLeft.setDrawGridLines(true)
                axisLeft.setDrawAxisLine(false)
                axisLeft.setDrawLabels(true)
                axisLeft.axisMinimum = 0f
                axisLeft.axisMaximum = 12f
                axisLeft.granularity = 1f
                axisLeft.setLabelCount(5, true)

                axisRight.setDrawGridLines(false)

                description.text = getString(R.string.chartTitle)
                description.textColor = this@HomeFragment.requireActivity().getColor(R.color.void_century)
                description.textSize = 20f

                //Legend (label chart)
                legend.isEnabled = true
                legend.textColor = this@HomeFragment.requireActivity().getColor(R.color.void_century)
                legend.form = Legend.LegendForm.CIRCLE
                legend.formSize = 10f
                legend.xEntrySpace = 40f
                legend.yEntrySpace = 10f
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                legend.textSize = 12f
                legend.xOffset = -50f

                legend.setCustom(listOf(
                    LegendEntry(SOLD, Legend.LegendForm.CIRCLE, 10f, 2f, null, this@HomeFragment.requireActivity().getColor(R.color.verditer)),
                    LegendEntry(STOCK, Legend.LegendForm.CIRCLE, 10f, 2f, null, this@HomeFragment.requireActivity().getColor(R.color.mythical_orange)),
                    LegendEntry("Expired", Legend.LegendForm.CIRCLE, 10f, 2f, null, this@HomeFragment.requireActivity().getColor(R.color.grenadine_pink))
                ))

                animateY(2000)
            }

        })

        homeViewModel.collectionAssetLocationResponse.observe(this@HomeFragment.requireActivity(), {response->
            Log.d(TAG, "asset by location response: $response")

            val gudangCount = response.retrieveValueBasedOnLocation(GUDANG)
            val rakCount = response.retrieveValueBasedOnLocation(RAK)

            binding.tvLocationValue1.text = gudangCount.toString()
            binding.tvLocationValue2.text = rakCount.toString()

            val barEntries = ArrayList<BarEntry>()
            barEntries.add(BarEntry(0f, gudangCount.toFloat())) // Index 0 untuk Sold
            barEntries.add(BarEntry(1f, rakCount.toFloat())) // Index 1 untuk Stock

            val barDataSet = BarDataSet(barEntries, "Kategori")
            barDataSet.colors = listOf(
                this@HomeFragment.requireActivity().getColor(R.color.verditer), // Warna untuk Sold
                this@HomeFragment.requireActivity().getColor(R.color.mythical_orange), // Warna untuk Stock
            )

            barDataSet.setDrawValues(false)

            val barData = BarData(barDataSet)
            barData.setValueTextColor(this@HomeFragment.requireActivity().getColor(R.color.void_century)) // Atur warna teks
            barData.setValueTextSize(16f) // Ukuran teks
            barData.barWidth = 0.3f


            binding.chartLocation.apply {
                data = barData
                setFitBars(true)

                //Description
                description.isEnabled = false
                axisLeft.textColor = this@HomeFragment.requireActivity().getColor(R.color.void_century)
                axisRight.isEnabled = false
                xAxis.textColor = this@HomeFragment.requireActivity().getColor(R.color.void_century)
                legend.textColor = this@HomeFragment.requireActivity().getColor(R.color.void_century)

                xAxis.granularity = 1f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.setDrawAxisLine(false)
                xAxis.setDrawLabels(false)

                axisLeft.setDrawGridLines(true)
                axisLeft.setDrawAxisLine(false)
                axisLeft.setDrawLabels(true)
                axisLeft.axisMinimum = 0f
                axisLeft.axisMaximum = 20f
                axisLeft.granularity = 1f
                axisLeft.setLabelCount(5, true)

                axisRight.setDrawGridLines(false)

                description.text = getString(R.string.chartTitle)
                description.textColor = this@HomeFragment.requireActivity().getColor(R.color.void_century)
                description.textSize = 20f

                //Legend (label chart)
                legend.isEnabled = true
                legend.textColor = this@HomeFragment.requireActivity().getColor(R.color.void_century)
                legend.form = Legend.LegendForm.CIRCLE
                legend.formSize = 10f
                legend.xEntrySpace = 50f
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                legend.xOffset = -30f
                legend.textSize = 12f

                legend.setCustom(listOf(
                    LegendEntry(GUDANG, Legend.LegendForm.CIRCLE, 10f, 2f, null, this@HomeFragment.requireActivity().getColor(R.color.verditer)),
                    LegendEntry(RAK, Legend.LegendForm.CIRCLE, 10f, 2f, null, this@HomeFragment.requireActivity().getColor(R.color.mythical_orange)),
//                    LegendEntry("Expired", Legend.LegendForm.CIRCLE, 10f, 2f, null, this@HomeFragment.requireActivity().getColor(R.color.grenadine_pink))
                ))

                animateY(2000)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
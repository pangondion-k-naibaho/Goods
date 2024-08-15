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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goods.client.R
import com.goods.client.data.Constants.PREFERENCES.Companion.APP_PREFERENCES
import com.goods.client.data.Constants.PREFERENCES.Companion.TOKEN_KEY
import com.goods.client.data.model.response.all_asset.ResultAssetResponse
import com.goods.client.data.remote.ApiConfig
import com.goods.client.data.repository.all_asset.AllAssetRepositoryImpl
import com.goods.client.data.repository.logout.LogoutRepositoryImpl
import com.goods.client.data.repository.profile.ProfileRepositoryImpl
import com.goods.client.databinding.FragmentAssetBinding
import com.goods.client.ui.activities.dashboard.FragmentsDashboardCommunicator
import com.goods.client.ui.activities.update_delete_asset.UpdateDeleteAssetActivity
import com.goods.client.ui.custom_components.InputSearchView
import com.goods.client.ui.rv_adapters.ItemAssetAdapter
import com.goods.client.ui.viewmodels.asset.AssetViewModel
import com.goods.client.ui.viewmodels.asset.AssetViewModelFactory
import com.goods.client.ui.viewmodels.dashboard.DashboardViewModel
import com.goods.client.ui.viewmodels.dashboard.DashboardViewModelFactory

class AssetFragment : Fragment() {
    private val TAG = AssetFragment::class.java.simpleName
    private lateinit var binding: FragmentAssetBinding
    private lateinit var fdCommunicator: FragmentsDashboardCommunicator

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var assetViewModel: AssetViewModel
    private lateinit var dashboardViewModel: DashboardViewModel
    private var userToken: String?= null
    private var page: Int?= null
    private var search: String = ""
    private var rvAdapter: ItemAssetAdapter?= null

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

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAssetBinding.bind(inflater.inflate(R.layout.fragment_asset, container, false))
        fdCommunicator = activity as FragmentsDashboardCommunicator

        val apiService = ApiConfig.createApiService()
        val allAssetRepository = AllAssetRepositoryImpl(apiService)

        val factory = AssetViewModelFactory(allAssetRepository)
        assetViewModel = ViewModelProvider(this@AssetFragment.requireActivity(), factory)[AssetViewModel::class.java]

        val profileRepository = ProfileRepositoryImpl(apiService)
        val logoutRepository = LogoutRepositoryImpl(apiService)
        val dashboardFactory = DashboardViewModelFactory(profileRepository, logoutRepository)
        dashboardViewModel = ViewModelProvider(this@AssetFragment.requireActivity(), factory)[DashboardViewModel::class.java]

        observeStatus()
        setUpView()

        return binding.root
    }

    private fun observeStatus(){
        assetViewModel.isLoading.observe(this@AssetFragment.requireActivity(), {
            if(it) fdCommunicator.startLoading() else fdCommunicator.stopLoading()
        })

        assetViewModel.isFail.observe(this@AssetFragment.requireActivity(), {
            if(it) Toast.makeText(this@AssetFragment.requireActivity(), "Failed to retrieve data...", Toast.LENGTH_SHORT).show()
        })
    }


    private fun setUpView(){
        page = 1
        sharedPreferences = this@AssetFragment.requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        Log.d(TAG, "userToken: $userToken")

        /*
         Below is the start of the code section that handles displaying data in the RecyclerView
        */

        dashboardViewModel.getProfile(userToken!!)

        dashboardViewModel.profileResponse.observe(this@AssetFragment.requireActivity(), {response->
            val editor = sharedPreferences.edit()
            editor.putString(TOKEN_KEY, response.refreshedToken)
            editor.apply()

            fdCommunicator.updateProfile(username = response.username, email = response.email)

        })

        userToken = sharedPreferences.getString(TOKEN_KEY, "")

        assetViewModel.getAllAsset(token = userToken!!, page!!, search)

        assetViewModel.allAssetResponse.observe(this@AssetFragment.requireActivity(), {response->
            Log.d(TAG, "asset response in page $page: ${response}")
            binding.rvAsset.apply {
                val rvLayoutManager = LinearLayoutManager(this@AssetFragment.requireActivity())
                layoutManager = rvLayoutManager

                rvAdapter = ItemAssetAdapter(
                    response.results.toMutableList(),
                    object: ItemAssetAdapter.ItemListener{
                        override fun onUpdateClicked(item: ResultAssetResponse) {
                            Log.d(TAG, "updated data: ${item}")
                            this@AssetFragment.requireActivity().startActivity(
                                UpdateDeleteAssetActivity.newIntent(this@AssetFragment.requireActivity(), item.id)
                            )
                            this@AssetFragment.requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        }
                    }
                )
                adapter = rvAdapter

                addOnScrollListener(object: RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val visibleItemCount = rvLayoutManager.childCount
                        val totalItemCount = rvLayoutManager.itemCount
                        val firstVisibleItemPosition = rvLayoutManager.findFirstVisibleItemPosition()

                        if((visibleItemCount+firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= response.results.size
                        ){
                            page = page?.plus(1)
                            assetViewModel.getAllAssetMore(userToken!!, page!!, search)
                            assetViewModel.allAssetResponse2.observe(this@AssetFragment.requireActivity(), {response2->
                                Log.d(TAG, "asset response in page $page: ${response2.results}")
                                if(response2.results.size != 0){
                                    rvAdapter!!.addItem(response2.results)
                                }
                            })
                        }
                    }
                })
            }
        })

        /*
         Above is the end of the code section that handles displaying data in the RecyclerView
        */

        /*
         Below is the start of the code section that handles search bar and stuffs
        */

        binding.isvAsset.apply {
            setIconClearInvisible()
            setTextHelper(this@AssetFragment.requireActivity().getString(R.string.isvTxtSearch))
            setListener(object: InputSearchView.InputSearchListener{
                override fun onClickSearch() {
                    val typedText = getText()
                    search = typedText

                    page = 1

                    assetViewModel.getAllAssetMore(userToken!!, page!!, search)
                    assetViewModel.allAssetResponse2.observe(this@AssetFragment.requireActivity(), {response->
                        Log.d(TAG, "result: ${response.results}")
                        if(!response.results.isNullOrEmpty()){
                            rvAdapter!!.updateItem(response.results)
                        }else{
                            Toast.makeText(
                                this@AssetFragment.requireActivity(),
                                getString(R.string.toastTextEmptyAssetSearch),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })

                }

                override fun onClearSearch() {
                    clearText()
                    page = 1
                    search = getText()
                    assetViewModel.getAllAssetMore(userToken!!, page!!, search)
                    assetViewModel.allAssetResponse2.observe(this@AssetFragment.requireActivity(), {response->
                        Log.d(TAG, "result: ${response.results}")
                        if(!response.results.isNullOrEmpty()){
                            rvAdapter!!.updateItem(response.results)
                        }else{
                            Toast.makeText(
                                this@AssetFragment.requireActivity(),
                                getString(R.string.toastTextEmptyAssetSearch),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }

            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        page = null
    }
}
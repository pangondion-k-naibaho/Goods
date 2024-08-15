package com.goods.client.ui.activities.dashboard

interface FragmentsDashboardCommunicator {
    fun startLoading()

    fun stopLoading()

    fun updateProfile(username: String, email: String)

}
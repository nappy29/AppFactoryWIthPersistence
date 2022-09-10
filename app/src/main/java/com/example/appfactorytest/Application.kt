package com.example.appfactorytest

import android.app.Application
import com.example.appfactorytest.util.NetworkMonitoringUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application() {

    var mNetworkMonitoringUtil: NetworkMonitoringUtil? = null
    override fun onCreate() {
        super.onCreate()

        mNetworkMonitoringUtil = NetworkMonitoringUtil(applicationContext);
        // Check the network state before registering for the 'networkCallbackEvents'
        mNetworkMonitoringUtil!!.checkNetworkState();
        mNetworkMonitoringUtil!!.registerNetworkCallbackEvents()

    }
}
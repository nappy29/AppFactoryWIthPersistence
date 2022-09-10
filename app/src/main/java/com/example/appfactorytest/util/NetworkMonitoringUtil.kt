package com.example.appfactorytest.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log

class NetworkMonitoringUtil(context: Context) : ConnectivityManager.NetworkCallback() {

    private val TAG: String = NetworkMonitoringUtil::class.java.name

    private var mNetworkRequest: NetworkRequest? = null
    private var mConnectivityManager: ConnectivityManager? = null
    private var mNetworkStateManager = NetworkStateManager

    init {
        mNetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build();

        mConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)

        Log.d(TAG, "onAvailable() called: Connected to network")
        mNetworkStateManager.setNetworkConnectivityStatus(true);
    }

    override fun onLost(network: Network) {
        super.onLost(network)

        Log.e(TAG, "onLost() called: Lost network connection");
        mNetworkStateManager.setNetworkConnectivityStatus(false);
    }

    fun registerNetworkCallbackEvents() {
        Log.d(TAG, "registerNetworkCallbackEvents() called");
        mConnectivityManager!!.registerNetworkCallback(mNetworkRequest!!, this)
    }

    fun checkNetworkState() {
        try {
            val networkInfo = mConnectivityManager!!.activeNetworkInfo
            // Set the initial value for the live-data
            mNetworkStateManager.setNetworkConnectivityStatus(
                networkInfo != null
                        && networkInfo.isConnected
            )
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
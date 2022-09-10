package com.example.appfactorytest.util

import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object NetworkStateManager {
    private val TAG: String = NetworkStateManager::class.java.name

    private val activeNetworkStatusMLD = MutableLiveData<Boolean>()


    /**
     * Updates the active network status live-data
     */
    fun setNetworkConnectivityStatus(connectivityStatus: Boolean) {
        Log.d(
            TAG,
            "setNetworkConnectivityStatus() called with: connectivityStatus = [$connectivityStatus]"
        )
        if (Looper.myLooper() == Looper.getMainLooper()) {
            activeNetworkStatusMLD.setValue(connectivityStatus)
        } else {
            activeNetworkStatusMLD.postValue(connectivityStatus)
        }
    }

    /**
     * Returns the current network status
     */
    fun getNetworkConnectivityStatus(): LiveData<Boolean?>? {
        Log.d(TAG, "getNetworkConnectivityStatus() called")
        return activeNetworkStatusMLD
    }
}
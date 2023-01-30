package com.mine.common.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetWorkUtils {

    fun isConnect(): Boolean {
        val service = AppUtils.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE)
        service as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = service.getNetworkCapabilities(service.activeNetwork)
            return (networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)))
        } else {
            return service.activeNetworkInfo?.isConnected ?: false
        }

    }
}
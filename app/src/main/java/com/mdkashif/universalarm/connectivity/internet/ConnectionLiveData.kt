package com.appworks.schooldeck.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.LiveData

class ConnectionLiveData(context: Context) : LiveData<ConnectionModel>() {

    private var context : Context = context

    override fun onActive() {
        super.onActive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, filter)
    }

    override  fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(networkReceiver)
    }

    private val networkReceiver = object: BroadcastReceiver() {
       override fun onReceive(context:Context, intent:Intent) {
            if (intent.extras != null)
            {
                val activeNetwork = intent.extras.get(ConnectivityManager.EXTRA_NETWORK_INFO) as NetworkInfo
                val isConnected = (activeNetwork.isConnectedOrConnecting)
                if (isConnected)
                {
                    when (activeNetwork.type) {
                        ConnectivityManager.TYPE_WIFI -> postValue(ConnectionModel(1, true))
                        ConnectivityManager.TYPE_MOBILE -> postValue(ConnectionModel(2, true))
                    }
                }
                else
                {
                    postValue(ConnectionModel(0, false))
                }
            }
        }
    }

}
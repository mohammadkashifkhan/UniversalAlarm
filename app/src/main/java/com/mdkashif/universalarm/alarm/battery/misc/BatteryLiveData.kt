package com.mdkashif.universalarm.alarm.battery.misc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

import androidx.lifecycle.LiveData

class BatteryLiveData(private val context: Context) : LiveData<BatteryStatsPoJo>() {
    internal var level: Int = 0
    internal var temp: Float = 0.toFloat()
    lateinit var status: String

    private val mBatteryInfoReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {

            val currLevel = intent.getIntExtra(
                    BatteryManager.EXTRA_LEVEL, -1)
            val maxLevel = intent.getIntExtra(
                    BatteryManager.EXTRA_SCALE, -1)
            level = Math.round(currLevel * 100.0 / maxLevel).toInt()

            status = if (isConnected(context))
                "Charging"
            else
                "Discharging"

            temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0).toFloat() / 10

            postValue(BatteryStatsPoJo(level, temp, status))
        }
    }

    override fun onActive() {
        super.onActive()
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(mBatteryInfoReceiver, filter)
    }

    override fun onInactive() {
        super.onInactive()
        context.unregisterReceiver(mBatteryInfoReceiver)
    }

    fun isConnected(context: Context): Boolean {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val plugged = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB
    }
}

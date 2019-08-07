package com.mdkashif.universalarm.alarm.battery.misc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.LiveData
import kotlin.math.roundToInt

class BatteryLiveData(private val context: Context) : LiveData<BatteryStatsPoJo>() {
    private var level = 0
    private var temp = 0F
    private var status = ""
    private var time = ""

    companion object {
        internal var timeToCharge = ""
        internal var timeToDischarge = ""
    }

    private val mBatteryInfoReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val batteryStats = BatteryStats(intent)

            val currLevel = batteryStats.level
            val maxLevel = batteryStats.scale
            level = (currLevel * 100.0 / maxLevel).roundToInt()

            when {
                batteryStats.isCharging -> {
                    time = timeToCharge
                    status = "Charging"
                }
                else -> {
                    time = timeToDischarge
                    status = "Discharging"
                }
            }

            temp = batteryStats.temperature.toFloat()

            postValue(BatteryStatsPoJo(level, temp, status, time))
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

    class BatteryTimeStats : BatteryTimeService() {
        override fun onChargingTimePublish(hours: Int, mins: Int) {
            //Called while charging and time to charge is being calculated.
            timeToCharge = "${hours}h ${mins}m"
        }

        override fun onCalculatingChargingTime() {
            //hours and mins params indicates remaining time for full charge.
        }

        override fun onDischargeTimePublish(days: Int, hours: Int, mins: Int) {
            //days, hours and mins params indicates remaining time for discharge.
            timeToDischarge = "${days}d ${hours}h ${mins}m"
        }

        override fun onCalculatingDischargingTime() {
            //Called while discharging and time to discharge is being calculated.
        }

        override fun onFullBattery() {
            //Called when device is charging and battery becomes full.
        }
    }
}

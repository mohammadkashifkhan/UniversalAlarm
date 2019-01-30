package com.mdkashif.universalarm.alarm.battery.misc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.BatteryManager

import com.mdkashif.universalarm.notifications.NotificationService
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder

class BatteryInfoReceiver : BroadcastReceiver() {

    private var highBatteryPercentage: Float? = null
    private var lowBatteryPercentage: Float? = null
    private var tempLevel: Float? = null
    private var currentBatteryPercentage: Float? = null
    private var currentBatteryLevel: Float = 0.toFloat()
    private var maxBatteryLevel: Float = 0.toFloat()
    lateinit var mAudioManager: AudioManager

    override fun onReceive(context: Context, intent: Intent) {
        highBatteryPercentage = SharedPrefHolder.getInstance(context).hbl
        lowBatteryPercentage = SharedPrefHolder.getInstance(context).lbl
        tempLevel = SharedPrefHolder.getInstance(context).temp

        currentBatteryLevel = intent.getIntExtra(
                BatteryManager.EXTRA_LEVEL, -1).toFloat()
        maxBatteryLevel = intent.getIntExtra(
                BatteryManager.EXTRA_SCALE, -1).toFloat()
        currentBatteryPercentage = Math.round(currentBatteryLevel * 100.0 / maxBatteryLevel).toFloat()

        mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // TODO: send to notification service
        if (currentBatteryPercentage!! >= highBatteryPercentage!! && SharedPrefHolder.getInstance(context).batteryAlarmStatus!!)
            if (isConnected(context))
                startAlarm("Unplug your Charger", "Your mobile is already $highBatteryPercentage% charged", context)

        if (currentBatteryPercentage!! <= lowBatteryPercentage!! && SharedPrefHolder.getInstance(context).batteryAlarmStatus!!)
            if (!isConnected(context))
                startAlarm("Plugin your Charger", "Battery has less than $lowBatteryPercentage% charge left", context)

        if (SharedPrefHolder.getInstance(context).theftAlarmStatus!!)
            if (!isConnected(context))
                startAlarm("Theft Alarm", "Someone just might be unplugging your phone!", context)

        if (getCurrentBatteryTemperature(context) > tempLevel!! && SharedPrefHolder.getInstance(context).temperatureAlarmStatus!!)
            startAlarm("Your Phone is getting too warm", "Either switch it off or unplug it", context)

    }

    private fun getCurrentBatteryTemperature(context: Context): Float {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        return intent!!.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0).toFloat() / 10
    }

    private fun isConnected(context: Context): Boolean {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val plugged = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB
    }

    private fun startAlarm(title: String, message: String, context: Context) {
        val alarmIntent = Intent(context, NotificationService::class.java)
        alarmIntent.putExtra("notificationTitle", title)
        alarmIntent.putExtra("notificationMessage", message)
        context.startService(alarmIntent)
    }

    companion object {
        private lateinit var mBatteryInfoReceiver: BatteryInfoReceiver

        val instance: BatteryInfoReceiver
            get() {
                mBatteryInfoReceiver = BatteryInfoReceiver()

                return mBatteryInfoReceiver
            }
    }

}


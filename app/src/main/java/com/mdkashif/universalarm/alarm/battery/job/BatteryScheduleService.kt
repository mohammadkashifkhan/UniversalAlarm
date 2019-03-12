package com.mdkashif.universalarm.alarm.battery.job

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import com.mdkashif.universalarm.alarm.misc.AlarmTypes
import com.mdkashif.universalarm.notifications.NotificationService
import com.mdkashif.universalarm.persistence.AppPreferences

class BatteryScheduleService : JobService() {
    private var highBatteryPercentage: Float = 0f
    private var lowBatteryPercentage: Float = 0f
    private var tempLevel: Float = 0f
    private var currentBatteryPercentage: Float = 0f
    private var currentBatteryLevel: Float = 0f
    private var maxBatteryLevel: Float = 0f

    override fun onStartJob(p0: JobParameters?): Boolean {
        highBatteryPercentage = AppPreferences.hbl!!
        lowBatteryPercentage = AppPreferences.lbl!!
        tempLevel = AppPreferences.temp!!

        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = applicationContext.registerReceiver(null, iFilter)

        currentBatteryLevel = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_LEVEL, -1).toFloat()
        maxBatteryLevel = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_SCALE, -1).toFloat()
        currentBatteryPercentage = Math.round(currentBatteryLevel * 100.0 / maxBatteryLevel).toFloat()

        if (currentBatteryPercentage >= highBatteryPercentage && AppPreferences.batteryAlarmStatus!!)
            if (isConnectedToCharge(applicationContext))
                startAlarm("Unplug your Charger", "Your mobile is already $highBatteryPercentage% charged", applicationContext)

        if (currentBatteryPercentage <= lowBatteryPercentage && AppPreferences.batteryAlarmStatus!!)
            if (!isConnectedToCharge(applicationContext))
                startAlarm("Plugin your Charger", "Battery has less than $lowBatteryPercentage% charge left", applicationContext)

        if (AppPreferences.theftAlarmStatus!!)
            if (!isConnectedToCharge(applicationContext))
                startAlarm("Theft Alarm", "Someone just might be unplugging your phone!", applicationContext)

        if (getCurrentBatteryTemperature(applicationContext) > tempLevel && AppPreferences.temperatureAlarmStatus!!)
            startAlarm("Your Phone is getting too warm", "Either switch it off or unplug it", applicationContext)

        jobFinished(p0, false)
        BatteryInfoScheduler.scheduleJob(applicationContext)
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    private fun getCurrentBatteryTemperature(context: Context): Float {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        return intent!!.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0).toFloat() / 10
    }

    private fun isConnectedToCharge(context: Context): Boolean {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val plugged = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB
    }

    private fun startAlarm(title: String, message: String, context: Context) {
        val alarmIntent = Intent(context, NotificationService::class.java)
        alarmIntent.putExtra("notificationTitle", title)
        alarmIntent.putExtra("notificationMessage", message)
        alarmIntent.putExtra("alarmType", AlarmTypes.Battery.toString())
        context.startService(alarmIntent)
    }
}
package com.mdkashif.universalarm.alarm.battery.job

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.content.IntentFilter
import com.mdkashif.universalarm.alarm.battery.misc.BatteryInfoReceiver

class BatteryScheduleService : JobService() {
    override fun onStartJob(p0: JobParameters?): Boolean {
        registerReceiver(BatteryInfoReceiver.instance, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        jobFinished(p0, false )
        BatteryInfoScheduler.scheduleJob(applicationContext) // TODO: not sure about registering it again and again
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        unregisterReceiver(BatteryInfoReceiver.getBatteryInfoReceiver())
        return false
    }
}
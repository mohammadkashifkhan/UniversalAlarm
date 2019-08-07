package com.mdkashif.universalarm.alarm.battery.misc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mdkashif.universalarm.alarm.misc.services.AlarmSoundService

class NotificationSwipeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context!!.stopService(Intent(context, AlarmSoundService::class.java))
    }
}
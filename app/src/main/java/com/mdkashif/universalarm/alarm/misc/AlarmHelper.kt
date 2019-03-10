package com.mdkashif.universalarm.alarm.misc

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mdkashif.universalarm.notifications.NotificationService
import java.util.*

object AlarmHelper {

    fun returnPendingIntentUniqueRequestCode(context: Context): Int {
        return (Math.random() * 9999999999999 + 1).toInt()
    }

    fun stopAlarm(requestCode: Int, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val mIntent = Intent(context,
                NotificationService::class.java)
        mIntent.putExtra("requestCode", requestCode)
        alarmManager.cancel(PendingIntent.getBroadcast(context, requestCode, mIntent, PendingIntent.FLAG_ONE_SHOT))
    }

    fun setAlarm(hour: Int, minute: Int, requestCode: Int, context: Context) { // for setting new alarms
        val dateNow = Date()//initializes to now
        val calAlarm = Calendar.getInstance()
        val calNow = Calendar.getInstance()
        calNow.time = dateNow
        calAlarm.time = dateNow
        calAlarm.set(Calendar.HOUR_OF_DAY, hour)
        calAlarm.set(Calendar.MINUTE, minute)

        if (calAlarm.before(calNow)) {
            calAlarm.add(Calendar.DATE, 1)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val mIntent = Intent(context,
                NotificationService::class.java)
        mIntent.putExtra("requestCode", requestCode)
        val pIntent = PendingIntent.getBroadcast(context, requestCode, mIntent, PendingIntent.FLAG_ONE_SHOT)

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, pIntent)
        } else if (Build.VERSION.SDK_INT >= 21) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, pIntent)
        }

        // TODO: send an alert Notification
    }
}

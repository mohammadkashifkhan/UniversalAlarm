package com.mdkashif.universalarm.alarm.misc

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mdkashif.universalarm.activities.ContainerActivity
import java.util.*

object AlarmHelper {

    fun returnPendingIntent(context: Context) : Int {
        // TODO: create pending intent with unique id here and that only will be used to stop alarm manager as well

        val dateNow = Date()//initializes to now
        val calAlarm = Calendar.getInstance()
        val calNow = Calendar.getInstance()
        calNow.time = dateNow
        calAlarm.time = dateNow
        calAlarm.set(Calendar.HOUR_OF_DAY, 2)
        calAlarm.set(Calendar.MINUTE, 35)
        calAlarm.set(Calendar.SECOND, 0)

        if (calAlarm.before(calNow)) {
            calAlarm.add(Calendar.DATE, 1)
        }

        val mIntent = Intent(context,
                ContainerActivity::class.java)
        val pIntent = PendingIntent.getBroadcast(context, 393, mIntent, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, pIntent)
        } else if (Build.VERSION.SDK_INT >= 21) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, pIntent)
        }
        return 0

        // TODO: send an alert Notification
    }
}

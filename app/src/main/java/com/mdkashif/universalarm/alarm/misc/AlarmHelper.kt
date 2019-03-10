package com.mdkashif.universalarm.alarm.misc

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mdkashif.universalarm.notifications.NotificationService
import java.util.*

object AlarmHelper {

    fun returnPendingIntentUniqueRequestCode(): Int {
        val r = Random()
        return r.nextInt(999999999 + 1) + 1
    }

    fun setAlarm(hour: Int, minute: Int, requestCode: Int, context: Context, types: AlarmTypes, note: String = "") {
        val dateNow = Date()
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
        var notificationTitle = StringBuilder().append("Alarm for ")
        var notificationMessage = ""
        when (types) {
            AlarmTypes.Time -> {
                notificationTitle.append("$hour:$minute")
                notificationMessage = note
            }
            AlarmTypes.Fajr -> {
                notificationTitle.append(types.toString())
                notificationMessage = ""
            }
            AlarmTypes.Dhuhr -> {
                notificationTitle.append(types.toString())
                notificationMessage = ""
            }
            AlarmTypes.Asr -> {
                notificationTitle.append(types.toString())
                notificationMessage = ""
            }
            AlarmTypes.Maghrib -> {
                notificationTitle.append(types.toString())
                notificationMessage = ""
            }
            AlarmTypes.Isha -> {
                notificationTitle.append(types.toString())
                notificationMessage = ""
            }
            AlarmTypes.Sunrise -> {
                notificationTitle.append(types.toString())
                notificationMessage = ""
            }
            AlarmTypes.Sunset -> {
                notificationTitle.append(types.toString())
                notificationMessage = ""
            }
            AlarmTypes.Imsak -> {
                notificationTitle.append(types.toString())
                notificationMessage = ""
            }
            AlarmTypes.Midnight -> {
                notificationTitle.append(types.toString())
                notificationMessage = ""
            }
        }
        mIntent.putExtra("notificationTitle", notificationTitle.toString())
        mIntent.putExtra("notificationMessage", notificationMessage)
        mIntent.putExtra("requestCode", requestCode)
        mIntent.putExtra("hour", hour)
        mIntent.putExtra("minute", minute)
        val pIntent = PendingIntent.getBroadcast(context, requestCode, mIntent, PendingIntent.FLAG_ONE_SHOT)

        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, pIntent)
        } else if (Build.VERSION.SDK_INT >= 21) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, pIntent)
        }
    }

    fun stopAlarm(requestCode: Int, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val mIntent = Intent(context,
                NotificationService::class.java)
        mIntent.putExtra("requestCode", requestCode)
        alarmManager.cancel(PendingIntent.getBroadcast(context, requestCode, mIntent, PendingIntent.FLAG_ONE_SHOT))
    }
}
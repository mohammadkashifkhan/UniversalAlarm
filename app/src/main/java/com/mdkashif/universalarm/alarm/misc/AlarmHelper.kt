package com.mdkashif.universalarm.alarm.misc

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mdkashif.universalarm.alarm.misc.model.DaysModel
import com.mdkashif.universalarm.alarm.misc.services.TimeIntentService
import java.util.*

object AlarmHelper {

    fun returnPendingIntentUniqueRequestCode(): Int {
        val r = Random()
        return r.nextInt(999999999 + 1) + 1
    }

    fun setAlarm(hour: Int, minute: Int, requestCode: Int, context: Context, types: AlarmTypes, note: String = "", repeat: Boolean = false, repeatDays: List<DaysModel>?) {
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
                TimeIntentService::class.java)
        var notificationTitle = StringBuilder().append("Alarm for ")
        var notificationMessage = ""
        when (types) {
            AlarmTypes.Time -> {
                notificationTitle.append("$hour:$minute")
                notificationMessage = note
            }
            AlarmTypes.Fajr -> {
                notificationTitle.append(types.toString())
                notificationMessage = "For a face that shines and illuminates, Get up and Pray!"
            }
            AlarmTypes.Dhuhr -> {
                notificationTitle.append(types.toString())
                notificationMessage = "For Blessed Wealth, Get up and Pray!"
            }
            AlarmTypes.Asr -> {
                notificationTitle.append(types.toString())
                notificationMessage = "For a Healthy, Strong body, Get up and Pray!"
            }
            AlarmTypes.Maghrib -> {
                notificationTitle.append(types.toString())
                notificationMessage = "For successful children, Get up and Pray!"
            }
            AlarmTypes.Isha -> {
                notificationTitle.append(types.toString())
                notificationMessage = "For Restful sleep, Get up and Pray!"
            }
            AlarmTypes.Sunrise -> {
                notificationTitle.append(types.toString())
                notificationMessage = "Just a friendly reminder!"
            }
            AlarmTypes.Sunset -> {
                notificationTitle.append(types.toString())
                notificationMessage = "Just a friendly reminder!"
            }
            AlarmTypes.Imsak -> {
                notificationTitle.append(types.toString())
                notificationMessage = "Just a friendly reminder!"
            }
            AlarmTypes.Midnight -> {
                notificationTitle.append(types.toString())
                notificationMessage = "Just a friendly reminder!"
            }
        }
        mIntent.putExtra("notificationTitle", notificationTitle.toString())
        mIntent.putExtra("notificationMessage", notificationMessage)
        mIntent.putExtra("requestCode", requestCode)
        mIntent.putExtra("hour", hour)
        mIntent.putExtra("minute", minute)
        mIntent.putExtra("alarmType", AlarmTypes.Time.toString()) // Its time alarm basically even if its subtype is prayer
        val pIntent = PendingIntent.getService(context, requestCode, mIntent, PendingIntent.FLAG_ONE_SHOT)

        if (repeat) {
            for (i in repeatDays!!.indices) {
                if (repeatDays[i].repeatDay == "Monday") {
                    calAlarm.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, AlarmManager.INTERVAL_DAY * 7, pIntent)
                }
                if (repeatDays[i].repeatDay == "Tuesday") {
                    calAlarm.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, AlarmManager.INTERVAL_DAY * 7, pIntent)
                }
                if (repeatDays[i].repeatDay == "Wednesday") {
                    calAlarm.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, AlarmManager.INTERVAL_DAY * 7, pIntent)
                }
                if (repeatDays[i].repeatDay == "Thursday") {
                    calAlarm.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, AlarmManager.INTERVAL_DAY * 7, pIntent)
                }
                if (repeatDays[i].repeatDay == "Friday") {
                    calAlarm.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, AlarmManager.INTERVAL_DAY * 7, pIntent)
                }
                if (repeatDays[i].repeatDay == "Saturday") {
                    calAlarm.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, AlarmManager.INTERVAL_DAY * 7, pIntent)
                }
                if (repeatDays[i].repeatDay == "Sunday") {
                    calAlarm.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, AlarmManager.INTERVAL_DAY * 7, pIntent)
                }
            }
        } else {
            if (types == AlarmTypes.Time) {
                if (Build.VERSION.SDK_INT >= 23) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, pIntent)
                } else if (Build.VERSION.SDK_INT >= 21) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, pIntent)
                }
            } else
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calAlarm.timeInMillis, AlarmManager.INTERVAL_DAY, pIntent)
        }
    }

    fun stopAlarm(requestCode: Int, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val mIntent = Intent(context,
                TimeIntentService::class.java)
        mIntent.putExtra("requestCode", requestCode)
        alarmManager.cancel(PendingIntent.getBroadcast(context, requestCode, mIntent, PendingIntent.FLAG_ONE_SHOT))
    }
}
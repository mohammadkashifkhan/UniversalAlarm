package com.mdkashif.universalarm.alarm.misc.services

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import com.github.omadahealth.lollipin.lib.managers.AppLock
import com.mdkashif.universalarm.activities.ContainerActivity
import com.mdkashif.universalarm.security.AntiTheftUnlockActivity
import com.mdkashif.universalarm.utils.AppConstants
import com.mdkashif.universalarm.utils.Utils

class TimeIntentService : IntentService("TimeIntentService") {
    private var bundleNotificationId = AppConstants.notificationId
    private var mIntent = Intent()

    public override fun onHandleIntent(intent: Intent?) {

        val notificationTitle = intent!!.getStringExtra("notificationTitle")
        val notificationMessage = intent.getStringExtra("notificationMessage")
        when (intent.getStringExtra("alarmType")) {
            "Time" -> {
                mIntent = Intent(this, ContainerActivity::class.java)
                mIntent.putExtra("hour", intent.getStringExtra("hour"))
                mIntent.putExtra("minute", intent.getStringExtra("minute"))
                mIntent.putExtra("note", notificationMessage)
                mIntent.putExtra("param1", "BuzzTimeAlarm")
                mIntent.putExtra("param2", intent.getStringExtra("requestCode"))
            }
            "Battery"->{
                mIntent = Intent(this, AntiTheftUnlockActivity::class.java)
                // TODO : stop alarm and what not everything basically in that activity, also dont have to show the pin directly only in case of theft, so choose the putextras wisely
                mIntent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN)
                mIntent.putExtra("param1", "BuzzBatteryAlarm")
            }
        }
        bundleNotificationId = +100
        val groupNotificationId = AppConstants.notificationChannelId + bundleNotificationId

        val pendingIntent = PendingIntent.getActivity(baseContext, bundleNotificationId, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        Utils.sendNotificationAlert(applicationContext,notificationTitle, notificationMessage, pendingIntent, groupNotificationId, bundleNotificationId)
    }
}

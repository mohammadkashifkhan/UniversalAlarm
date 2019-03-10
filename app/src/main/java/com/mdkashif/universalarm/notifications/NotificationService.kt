package com.mdkashif.universalarm.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.github.omadahealth.lollipin.lib.managers.AppLock
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.ContainerActivity
import com.mdkashif.universalarm.alarm.misc.AlarmSoundService
import com.mdkashif.universalarm.security.AntiTheftUnlockActivity
import com.mdkashif.universalarm.utils.AppConstants

class NotificationService : IntentService("NotificationService") {
    private var bundleNotificationId = AppConstants.notificationId
    private lateinit var mIntent : Intent

    public override fun onHandleIntent(intent: Intent?) {

        val notificationTitle = intent!!.getStringExtra("notificationTitle")
        val notificationMessage = intent.getStringExtra("notificationMessage")
        when (intent.getStringExtra("alarmType")) {
            "Time" -> {
                mIntent = Intent(this, ContainerActivity::class.java)
                mIntent.putExtra("hour", intent.getStringExtra("hour"))
                mIntent.putExtra("minute", intent.getStringExtra("minute"))
                mIntent.putExtra("note", notificationMessage)
                mIntent.putExtra("param1", "BuzzAlarmFragment")
                mIntent.putExtra("param2", intent.getStringExtra("requestCode"))
            }
            "Battery"->{
                mIntent = Intent(this, AntiTheftUnlockActivity::class.java)
                // TODO : stop alarm and what not everything basically in that activity,
                // TODO : also dont have to show the pin directly only in case of theft, so choose the putextras wisely
                mIntent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN)
                mIntent.putExtra("param1", "BuzzAlarmFragment")
            }
        }
        bundleNotificationId = +100
        val groupNotificationId = AppConstants.notificationChannelId + bundleNotificationId

        val pendingIntent = PendingIntent.getActivity(baseContext, bundleNotificationId, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        sendNotificationAlert(notificationTitle, notificationMessage, pendingIntent, groupNotificationId, bundleNotificationId)
    }

    private fun sendNotificationAlert(title: String, message: String, pendingIntent: PendingIntent, groupNotificationId: String, bundleNotificationId: Int) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(AppConstants.notificationChannelId, AppConstants.notificationChannelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.setShowBadge(true)
            notificationChannel.description = AppConstants.notificationDescription
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = applicationContext.getColor(R.color.red)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // to display notification in DND Mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(AppConstants.notificationChannelId)
            channel.canBypassDnd()
        }

        val notificationBuilder = NotificationCompat.Builder(this, AppConstants.notificationChannelId)
        notificationBuilder
                .setContentTitle(title)
                .setContentText(message)
                .setGroup(groupNotificationId)
                .setGroupSummary(true)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_alarm)
                .setAutoCancel(false)

        val notification: Notification = notificationBuilder.build()
        notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT // non cancellable until tapped, should be only for theft alarms
        notification.flags = notification.flags or Notification.FLAG_INSISTENT // repeat unless tapped and cancelled

        notificationManager.notify(bundleNotificationId, notification)

        applicationContext.startService(Intent(applicationContext, AlarmSoundService::class.java))
    }
}

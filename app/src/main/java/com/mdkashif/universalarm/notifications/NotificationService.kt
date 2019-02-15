package com.mdkashif.universalarm.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.activities.ContainerActivity
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmSoundService
import com.mdkashif.universalarm.utils.AppConstants

class NotificationService : IntentService("NotificationService") {
    private var bundleNotificationId = AppConstants.notificationId

    public override fun onHandleIntent(intent: Intent?) {
        val mIntent = Intent(this, ContainerActivity::class.java)
        val notificationTitle = intent!!.getStringExtra("notificationTitle")
        val notificationMessage = intent.getStringExtra("notificationMessage")
        mIntent.putExtra("param", "BuzzAlarm")

        bundleNotificationId = +100
        val groupNotificationId = AppConstants.notificationChannelId + bundleNotificationId

        val pendingIntent = PendingIntent.getActivity(baseContext, bundleNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        sendNotificationAlert(notificationTitle, notificationMessage, pendingIntent, groupNotificationId, bundleNotificationId)
    }

    private fun sendNotificationAlert(title: String, message: String, pendingIntent: PendingIntent, groupNotificationId: String, bundleNotificationId: Int) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(AppConstants.notificationChannelId, AppConstants.notificationChannelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.setShowBadge(true)
            notificationChannel.description = AppConstants.notificationDescription
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
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

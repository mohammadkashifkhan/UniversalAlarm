package com.mdkashif.alarm.notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mdkashif.alarm.R
import com.mdkashif.alarm.activities.ContainerActivity
import com.mdkashif.alarm.utils.AppConstants
import com.mdkashif.alarm.utils.persistence.SharedPrefHolder

class NotificationService : IntentService("NotificationService") {
    private var bundleNotificationId= AppConstants.notificationId as Int
    private var defaults: Int = 0
    private var uri: Uri? = null

    public override fun onHandleIntent(intent: Intent?) {
        val mIntent = Intent(this, ContainerActivity::class.java)
        mIntent.putExtra("param", "BuzzAlarm")

        bundleNotificationId = +100
        val groupNotificationId = AppConstants.notificationChannelId+bundleNotificationId

        val pendingIntent = PendingIntent.getActivity(baseContext, bundleNotificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // TODO: check the type of alarm here and take appropriate actions
        // TODO: combine notification sound and notification sound ie, only one sound should play at all time

        if (SharedPrefHolder.getInstance(applicationContext).ringStatus || SharedPrefHolder.getInstance(applicationContext).vibrateStatus) {
            if (SharedPrefHolder.getInstance(applicationContext).ringStatus && SharedPrefHolder.getInstance(applicationContext).vibrateStatus) {
                if (SharedPrefHolder.getInstance(applicationContext).ringtoneUri != "" && SharedPrefHolder.getInstance(applicationContext).vibrateStatus) {
                    uri = Uri.parse(SharedPrefHolder.getInstance(applicationContext).ringtoneUri)
                    defaults = Notification.DEFAULT_VIBRATE
                    sendNotificationAlert("Universal Alarm", "Wake Up! Wake Up! Alarm started!!", pendingIntent, groupNotificationId, bundleNotificationId)
                }
                else if (SharedPrefHolder.getInstance(applicationContext).ringtoneUri == "" && SharedPrefHolder.getInstance(applicationContext).vibrateStatus) {
                    defaults = Notification.DEFAULT_ALL
                    sendNotificationAlert("Universal Alarm", "Wake Up! Wake Up! Alarm started!!", pendingIntent, groupNotificationId, bundleNotificationId)
                }
            } else if (SharedPrefHolder.getInstance(applicationContext).ringStatus && !SharedPrefHolder.getInstance(applicationContext).vibrateStatus) {
                if (SharedPrefHolder.getInstance(applicationContext).ringtoneUri != "") {
                    uri = Uri.parse(SharedPrefHolder.getInstance(applicationContext).ringtoneUri)
                    sendNotificationAlert("Universal Alarm", "Wake Up! Wake Up! Alarm started!!", pendingIntent, groupNotificationId, bundleNotificationId)
                }
                else if (SharedPrefHolder.getInstance(applicationContext).ringtoneUri == "") {
                    defaults = Notification.DEFAULT_SOUND
                    sendNotificationAlert("Universal Alarm", "Wake Up! Wake Up! Alarm started!!", pendingIntent, groupNotificationId, bundleNotificationId)
                }
            } else
                defaults = Notification.DEFAULT_VIBRATE
        }
    }

    private fun sendNotificationAlert(title : String, message : String, pendingIntent: PendingIntent, groupNotificationId: String, bundleNotificationId: Int){
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
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_alarm)
                .setAutoCancel(true)

        val notification : Notification = notificationBuilder.build()
        notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT // non cancellable until tapped, should be only for theft alarms
        notification.flags = notification.flags or Notification.FLAG_INSISTENT // repeat unless tapped and cancelled

        notificationManager.notify(bundleNotificationId, notification)
    }
}

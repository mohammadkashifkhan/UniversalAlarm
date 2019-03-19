package com.mdkashif.universalarm.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.animation.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.snackbar.Snackbar
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.misc.AlarmSoundService
import com.mdkashif.universalarm.base.BaseActivity

object Utils {
    private lateinit var progressDialog: MaterialDialog

    fun sendNotificationAlert(context: Context, title: String, message: String, pendingIntent: PendingIntent, groupNotificationId: String, bundleNotificationId: Int, theftAlarm: Boolean = false) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(AppConstants.notificationChannelId, AppConstants.notificationChannelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.canBypassDnd()
            notificationChannel.setShowBadge(true)
            notificationChannel.description = AppConstants.notificationDescription
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = context.getColor(R.color.red)
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationChannel.enableVibration(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(context, AppConstants.notificationChannelId)
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
        if (theftAlarm) {
            notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT // non cancellable until tapped, should be only for theft alarms
            notification.flags = notification.flags or Notification.FLAG_INSISTENT // repeat unless tapped and cancelled
        }
        notificationManager.notify(bundleNotificationId, notification)

        context.startService(Intent(context, AlarmSoundService::class.java))
    }

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < 23) {
            val ni = cm.activeNetworkInfo
            if (ni != null) {
                return (ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE))
            }
        } else {
            val n = cm.activeNetwork
            if (n != null) {
                val nc = cm.getNetworkCapabilities(n)
                return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
            }
        }
        return false
    }

    fun showLoader(context: Context) {
        progressDialog = MaterialDialog(context).show {
            cancelable(false)
            customView(R.layout.layout_dialog_custom_progress)
        }
    }

    fun hideLoader() {
        progressDialog.dismiss()
    }

    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(message: String, view: View) {
        val sBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val sBarView = sBar.view
        val textView = sBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(view.context.resources.getColor(R.color.bg))
        sBar.show()
    }

    fun setRVSlideInLeftAnimation(view: RecyclerView) {
        val set = AnimationSet(true)
        var animation: Animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 100
        set.addAnimation(animation)
        animation = TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        animation.duration = 800
        set.addAnimation(animation)

        val controller = LayoutAnimationController(set, 0.5f)
        view.layoutAnimation = controller
    }

    fun sendFeedback(context: BaseActivity) {
        var body: String? = null
        try {
            body = context.packageManager.getPackageInfo(context.packageName, 0).versionName
            body = ("\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER)
        } catch (e: PackageManager.NameNotFoundException) {
        }

        val addresses = "mohammadkshf2093@gmail.com"
        val uri = Uri.parse("mailto:$addresses").buildUpon().build()
        val mIntent = Intent(Intent.ACTION_SENDTO, uri)
        mIntent.putExtra(Intent.EXTRA_SUBJECT, "Query from Universal Alarm app")
        mIntent.putExtra(Intent.EXTRA_TEXT, body)
        context.executeIntent(mIntent, false)
    }
}
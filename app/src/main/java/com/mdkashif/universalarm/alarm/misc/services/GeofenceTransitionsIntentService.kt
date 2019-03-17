package com.mdkashif.universalarm.alarm.misc.services

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.GeofencingEvent
import com.mdkashif.universalarm.activities.ContainerActivity
import com.mdkashif.universalarm.alarm.location.misc.GeofenceErrorMessages
import com.mdkashif.universalarm.utils.AppConstants
import com.mdkashif.universalarm.utils.Utils

class GeofenceTransitionsIntentService : IntentService("GeofenceTransitionIntentService") {
    private var mIntent = Intent()
    private var bundleNotificationId = AppConstants.notificationId

    override fun onHandleIntent(intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.errorCode)
            Log.e("check123123", errorMessage)
            return
        }
        mIntent = Intent(this, ContainerActivity::class.java)
        val notificationTitle = "We arrived! :]"
        val notificationMessage = intent!!.getStringExtra("note")

        mIntent.putExtra("param1", "BuzzLocationAlarm")
        mIntent.putExtra("latitude", intent.getStringExtra("latitude"))
        mIntent.putExtra("longitude", intent.getStringExtra("longitude"))

        bundleNotificationId = +100
        val groupNotificationId = AppConstants.notificationChannelId + bundleNotificationId

        val pendingIntent = PendingIntent.getActivity(baseContext, bundleNotificationId, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        Utils.sendNotificationAlert(applicationContext, notificationTitle, notificationMessage, pendingIntent, groupNotificationId, bundleNotificationId)
    }
}
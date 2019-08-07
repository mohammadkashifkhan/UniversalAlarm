package com.mdkashif.universalarm.alarm.misc.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.os.Vibrator
import com.mdkashif.universalarm.persistence.AppPreferences
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException


class AlarmSoundService : Service(), KoinComponent {

    private val appPreferences: AppPreferences by inject()

    private lateinit var mediaPlayer: MediaPlayer
    private var vibrator: Vibrator? = null

    override fun onCreate() {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build())
        try {
            if (appPreferences.ringtoneUri != "")
                mediaPlayer.setDataSource(this, Uri.parse(appPreferences.ringtoneUri))

            if (appPreferences.vibrateStatus)
                vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            mediaPlayer.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (!mediaPlayer.isPlaying) {
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()
        }

        if (appPreferences.vibrateStatus)
            vibrator!!.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}


package com.mdkashif.universalarm.alarm.misc

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.os.Vibrator
import com.mdkashif.universalarm.persistence.AppPreferences
import java.io.IOException


class AlarmSoundService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private var vibrator: Vibrator? = null

    override fun onCreate() {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build())
        try {
            if (AppPreferences().instance.ringtoneUri != "")
                mediaPlayer.setDataSource(this, Uri.parse(AppPreferences().instance.ringtoneUri))

            if (AppPreferences().instance.vibrateStatus)
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

        if (AppPreferences().instance.vibrateStatus)
            vibrator!!.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}


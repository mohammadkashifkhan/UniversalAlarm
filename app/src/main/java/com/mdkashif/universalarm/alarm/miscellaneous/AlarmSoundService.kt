package com.mdkashif.universalarm.alarm.miscellaneous

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.os.Vibrator
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder
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
            if (SharedPrefHolder.getInstance(applicationContext).ringtoneUri != "")
                mediaPlayer.setDataSource(this, Uri.parse(SharedPrefHolder.getInstance(applicationContext).ringtoneUri))

            if (SharedPrefHolder.getInstance(applicationContext).vibrateStatus)
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

        if (SharedPrefHolder.getInstance(applicationContext).vibrateStatus)
            vibrator!!.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

}


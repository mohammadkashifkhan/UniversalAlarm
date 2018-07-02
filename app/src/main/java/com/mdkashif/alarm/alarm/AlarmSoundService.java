package com.mdkashif.alarm.alarm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mdkashif.alarm.R;

public class AlarmSoundService extends Service {

    MediaPlayer mediaPlayer;
    @Override
    public void onCreate() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarmtone);

        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}


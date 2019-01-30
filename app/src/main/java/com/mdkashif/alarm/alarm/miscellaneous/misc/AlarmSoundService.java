package com.mdkashif.alarm.alarm.miscellaneous.misc;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import com.mdkashif.alarm.utils.persistence.SharedPrefHolder;

import java.io.IOException;

import androidx.annotation.Nullable;

public class AlarmSoundService extends Service {

    MediaPlayer mediaPlayer;
    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        try {
            if(!SharedPrefHolder.getInstance(getApplicationContext()).getRingtoneUri().equals(""))
                mediaPlayer.setDataSource(this, Uri.parse(SharedPrefHolder.getInstance(getApplicationContext()).getRingtoneUri()));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

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


package com.mdkashif.alarm.alarm.battery.misc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.BatteryManager;

import com.mdkashif.alarm.R;
import com.mdkashif.alarm.activities.ContainerActivity;
import com.mdkashif.alarm.alarm.miscellaneous.misc.AlarmSoundService;
import com.mdkashif.alarm.utils.persistence.SharedPrefHolder;

import androidx.core.app.NotificationCompat;

public class BatteryReceiver extends BroadcastReceiver {

    Float HBL,LBL,TEMP,level;
    float temp,currLevel,maxLevel;
    AudioManager mAudioManager;

    private static BatteryReceiver mBatteryReceiver;

    public BatteryReceiver() {
    }

    public static BatteryReceiver getInstance() {
        if (mBatteryReceiver == null) {
            mBatteryReceiver = new BatteryReceiver();
        }
        return mBatteryReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        HBL= SharedPrefHolder.getInstance(context).getHBL();
        LBL= SharedPrefHolder.getInstance(context).getLBL();
        TEMP= SharedPrefHolder.getInstance(context).getTemp();

        currLevel = intent.getIntExtra(
                BatteryManager.EXTRA_LEVEL, -1);
        maxLevel = intent.getIntExtra(
                BatteryManager.EXTRA_SCALE, -1);
        level = (float) Math.round((currLevel * 100.0) / maxLevel);

        mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

        // TODO: send to notification service
        if ((level >= HBL)&&SharedPrefHolder.getInstance(context).getBatteryAlarmStatus())
            if(isConnected(context))
                notif("Unplug your Charger", "Your mobile is already " + HBL + "% charged", context);

        if ((level <= LBL)&&SharedPrefHolder.getInstance(context).getBatteryAlarmStatus())
            if(!isConnected(context))
                notif("Plugin your Charger", "Battery has less than " + LBL + "% charge left", context);

        if(SharedPrefHolder.getInstance(context).getTheftAlarmStatus())
            if(!isConnected(context))
                notif("Theft Alarm", "Someone just might be unplugging your phone!", context);

        if((getBatteryTemperature(context)>TEMP)&&SharedPrefHolder.getInstance(context).getTemperatureAlarmStatus())
            notif("Your Phone is getting too warm","Either switch it off or unplug it",context);

    }

    public float getBatteryTemperature(Context context)
    {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        temp = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0)) / 10;
        return temp;
    }

    public boolean isConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }

    public void notif(String title, String msg, Context context){
        Intent intent = new Intent(context, ContainerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_alarm)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(msg)
                .setContentIntent(pendingIntent);

        mNotificationManager.notify(0, mBuilder.build());

        context.startService(new Intent(context,AlarmSoundService.class));
    }

}


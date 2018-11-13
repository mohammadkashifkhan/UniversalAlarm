package com.mdkashif.alarm.alarm.battery;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.BatteryManager;

import com.mdkashif.alarm.R;
import com.mdkashif.alarm.activities.ContainerActivity;
import com.mdkashif.alarm.alarm.AlarmSoundService;

import androidx.core.app.NotificationCompat;

import static android.content.Context.MODE_PRIVATE;

public class BatteryReceiver extends BroadcastReceiver {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String HBL,LBL,TEMP;
    float temp,currLevel,maxLevel;
    int level;
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
        sharedPreferences=context.getSharedPreferences("MyPrefs",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        HBL=sharedPreferences.getString("HBL","0");
        LBL=sharedPreferences.getString("LBL","0");
        TEMP=sharedPreferences.getString("TEMP","0");

        currLevel = intent.getIntExtra(
                BatteryManager.EXTRA_LEVEL, -1);
        maxLevel = intent.getIntExtra(
                BatteryManager.EXTRA_SCALE, -1);
        level = (int) Math.round((currLevel * 100.0) / maxLevel);
        HBL=HBL.replace("%","");
        LBL=LBL.replace("%","");
        TEMP=TEMP.replace("℃","");

        mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

            if ((level >= Integer.parseInt(HBL))&&(!("0".trim().equals(HBL)))&&("true".trim().equals(sharedPreferences.getString("Alarmstatus","")))) {
                if(isConnected(context)) {
                    notif("Unplug your Charger", "Your mobile is already " + HBL + "% charged", context);
                    editor.putString("alarmringing", "true");
                    editor.commit();
                }
            }

            if ((level <= Integer.parseInt(LBL))&&(!("0".trim().equals(LBL)))&&("true".trim().equals(sharedPreferences.getString("Alarmstatus","")))) {
                if(!isConnected(context)) {
                    notif("Plugin your Charger", "Battery has less than " + LBL + "% charge left", context);
                    editor.putString("alarmringing", "true");
                    editor.commit();
                }
            }

            if("true".equals(sharedPreferences.getString("enabletheftalarm",""))){
                if(!isConnected(context)) {
                    notif("Theft Alarm", "Someone just might be unplugging your phone!", context);
                    editor.putString("theftringing", "true");
                    editor.commit();
                }
            }

            if(((int)batteryTemperature(context))>(Integer.parseInt(TEMP))&&("true".equals(sharedPreferences.getString("Tempstatus","")))){
                notif("Your Phone is getting too warm","Either switch it off or unplug it",context);
                editor.putString("tempringing", "true");
                editor.commit();
            }
    }

    public float batteryTemperature(Context context)
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
                context).setSmallIcon(R.drawable.speakers)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentText(msg)
                .setContentIntent(pendingIntent);

        mNotificationManager.notify(0, mBuilder.build());

        switch (mAudioManager.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                if(("true".equals(sharedPreferences.getString("overridesilent",""))))
                {
                    context.startService(new Intent(context,AlarmSoundService.class));
                }
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                if(("true".equals(sharedPreferences.getString("overridesilent",""))))
                {
                    context.startService(new Intent(context,AlarmSoundService.class));
                }
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                context.startService(new Intent(context,AlarmSoundService.class));
                break;
            }

        }

    }


package com.mdkashif.alarm.notifications;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.mdkashif.alarm.R;
import com.mdkashif.alarm.activities.ShowBuzzingAlarmActivity;
import com.mdkashif.alarm.utils.SharedPrefHolder;

public class NotificationService extends IntentService {

    private int defaults;
    private Uri uri;
    //Notification ID for Alarm
    public static final int NOTIFICATION_ID = 786;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        intent=new Intent(this, ShowBuzzingAlarmActivity.class);
        //Send notification
        if(SharedPrefHolder.getInstance(getApplicationContext()).getringstatus() || SharedPrefHolder.getInstance(getApplicationContext()).getvibratestatus()) {
            if (SharedPrefHolder.getInstance(getApplicationContext()).getringstatus()&& SharedPrefHolder.getInstance(getApplicationContext()).getvibratestatus())
            {
                if ((!SharedPrefHolder.getInstance(getApplicationContext()).geturi().equals("")) && SharedPrefHolder.getInstance(getApplicationContext()).getvibratestatus()) {
                    uri = Uri.parse(SharedPrefHolder.getInstance(getApplicationContext()).geturi());
                    defaults = Notification.DEFAULT_VIBRATE;
                    showNotification("Universal Alarm","Wake Up! Wake Up! Alarm started!!",intent,"custom");
                }

                if ((SharedPrefHolder.getInstance(getApplicationContext()).geturi().equals("")) && SharedPrefHolder.getInstance(getApplicationContext()).getvibratestatus()) {
                    defaults = Notification.DEFAULT_ALL;
                    showNotification("Universal Alarm","Wake Up! Wake Up! Alarm started!!",intent,"normal");
                }
            }
            else if(SharedPrefHolder.getInstance(getApplicationContext()).getringstatus()&&(!SharedPrefHolder.getInstance(getApplicationContext()).getvibratestatus()))
            {
                if (!SharedPrefHolder.getInstance(getApplicationContext()).geturi().equals("")) {
                    uri = Uri.parse(SharedPrefHolder.getInstance(getApplicationContext()).geturi());
                    showNotification("Universal Alarm","Wake Up! Wake Up! Alarm started!!",intent,"custom");
                }

                if (SharedPrefHolder.getInstance(getApplicationContext()).geturi().equals("")) {
                    defaults = Notification.DEFAULT_SOUND;
                    showNotification("Universal Alarm","Wake Up! Wake Up! Alarm started!!",intent,"normal");
                }
            }
            else
                defaults=Notification.DEFAULT_VIBRATE;
        }
    }

    public void showNotification( String title, String body, Intent intent, String type) {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channel-786";
        String channelName = "universal_alarm_channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(body)
                .setWhen(System.currentTimeMillis())
                .setDefaults(defaults);
        if(type.equals("custom"))
            mBuilder.setSound(uri);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}

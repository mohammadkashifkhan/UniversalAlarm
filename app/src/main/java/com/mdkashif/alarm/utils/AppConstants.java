package com.mdkashif.alarm.utils;

/**
 * Created by Kashif on 16-Apr-18.
 */
public class AppConstants {

    /////// Endpoints ///////
    private static String MAIN_URL = "http://www.mdkashif.com/UniversalAlarm/";
    public static String PRAYER_API = "http://api.aladhan.com/v1/";
    public static String FAQ= MAIN_URL+"faq.html";
    public static String PP= MAIN_URL+"privacy-policy.html";
    public static String TNC= MAIN_URL+"terms-and-conditions.html";

    /////// Miscellaneous ///////
    public final static String isFirstTimeLaunch = "isFirstTimeLaunch";
    public final static String ringStatus = "ring_status";
    public final static String ringtoneUri = "ringtone_uri";
    public final static String vibrateStatus = "vibrate_status";

    public final static String alarmTypeLocation = "location";
    public final static String alarmTypeBattery = "battery";
    public final static String alarmTypeTime = "time";
    public final static String alarmTypePrayer = "prayer";

    public final static String themeLight = "Light";
    public final static String themeDark = "Dark";
    public final static String themeSelected = "theme";

    public final static String city = "city";
    public final static String country = "country";

    /////// SetBatteryLevelFragment ///////
    public final static String highBatteryLevel = "hbl";
    public final static String lowBatteryLevel = "lbl";
    public final static String batteryTempLevel = "temp";
    public final static String batteryAlarmStatus = "batteryAlarmStatus";
    public final static String theftAlarmStatus = "theftAlarmStatus";

    /////// Notification ///////
    public final static Integer notificationId = 786;
    public final static String notificationChannelName = "Miscellaneous";
    public final static String notificationChannelId = "com.mdkashif.alarm.notifs";
    public final static String notificationDescription = "Alarms";

}

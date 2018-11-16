package com.mdkashif.alarm.utils;

/**
 * Created by Kashif on 16-Apr-18.
 */
public class AppConstants {

    /////// Endpoints ///////
    public static String MAIN_URL = "http://www.mdkashif.com/UniversalAlarm/";
    public static String PRAYER_API = "http://api.aladhan.com/v1/";
    public static String FAQ= MAIN_URL+"faq.html";
    public static String PP= MAIN_URL+"privacy-policy.html";
    public static String TNC= MAIN_URL+"terms-and-conditions.html";

    /////// Miscellaneous ///////
    public static String isFirstTimeLaunch = "isFirstTimeLaunch";
    public static String ringStatus = "ring_status";
    public static String ringtoneUri = "ringtone_uri";
    public static String vibrateStatus = "vibrate_status";

    public final static String alarmTypeLocation = "location";
    public final static String alarmTypeBattery = "battery";
    public final static String alarmTypeTime = "time";
    public final static String alarmTypePrayer = "prayer";

    /////// SetBatteryLevelFragment ///////
    public static String highBatteryLevel = "hbl";
    public static String lowBatteryLevel = "lbl";
    public static String batteryTempLevel = "temp";
    public static String batteryAlarmStatus = "batteryAlarmStatus";
    public static String theftAlarmStatus = "theftAlarmStatus";
    public static String themeSelected = "theme";

    /////// Notification ///////
    public static final Integer notificationId = 786;
    public static final String notificationChannelName = "Miscellaneous";
    public static final String notificationChannelId = "com.mdkashif.alarm.notifs";
    public static final String notificationDescription = "Alarms";

}
